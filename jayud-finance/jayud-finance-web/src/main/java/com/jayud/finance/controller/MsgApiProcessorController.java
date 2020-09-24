package com.jayud.finance.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.annotations.IsFee;
import com.jayud.finance.bo.APARDetailForm;
import com.jayud.finance.bo.PayableHeaderForm;
import com.jayud.finance.bo.ReceivableHeaderForm;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.*;
import com.jayud.finance.service.BaseService;
import com.jayud.finance.service.ICustomsFinanceCoRelationService;
import com.jayud.finance.service.ICustomsFinanceFeeRelationService;
import com.jayud.finance.service.KingdeeService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 接收消息处理中心的feign请求
 *
 * @author william
 * @description
 * @Date: 2020-09-19 11:44
 */
@RestController
@RequestMapping("/api/finance/kingdee")
@Slf4j
public class MsgApiProcessorController {
    @Autowired
    KingdeeService kingdeeService;
    @Autowired
    BaseService baseService;
    @Autowired
    ICustomsFinanceCoRelationService coRelationService;
    @Autowired
    ICustomsFinanceFeeRelationService feeRelationService;
    @Autowired
    RedisUtils redisUtils;
    @Value("${relation-setting.redis-key.yunbaoguan.company}")
    String yunbaoguanCompKey;
    @Value("${relation-setting.redis-key.yunbaoguan.fee}")
    String yunbaoguanFeeKey;


    /**
     * 处理云报关的应收推送到金蝶接口
     * by william
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/yunbaoguan/receivable/push", method = RequestMethod.POST)
    @ApiOperation(value = "接收云报关的应收单信息推至金蝶")
    public Boolean saveReceivableBill(@RequestBody Map<String, String> msg) {
        /**
         * 根据财务要求，税率改为1%
         * 根据财务要求，应收单才记税率，应付单不记税率
         * 根据财务要求，应收单中如果遇到费用项目的类别为代垫税金，费用类型为代收代垫的，均进行标记且不记税率
         **/

        String reqMsg = MapUtil.getStr(msg, "msg");
        log.info("金蝶接收到报关应收数据：{}", reqMsg);

        //feign调用之前确保从列表中取出单行数据且非空，因此此处不需再校验
        List<CustomsReceivable> customsReceivable = JSONObject.parseArray(reqMsg, CustomsReceivable.class);

        //重单校验,只要金蝶中有数据就不能再次推送
        Optional<CustomsReceivable> first = customsReceivable.stream().filter(Objects::isNull).findFirst();
        if (first.isPresent() && checkForDuplicateOrder(first.get().getCustomApplyNo())) {
            return true;
        }

        //基本校验完毕，装载预制数据
        Map<String, CustomsFinanceCoRelation> coRelationMap = null;
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = null;
        String compJson = redisUtils.get(yunbaoguanCompKey);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = JSONObject.parseObject(compJson, Map.class);
        } else {
            coRelationMap = coRelationService.list().stream().collect(Collectors.toMap(CustomsFinanceCoRelation::getYunbaoguanName, e -> e));
            redisUtils.set(yunbaoguanCompKey, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        String feeJson = redisUtils.get(yunbaoguanFeeKey);
        if (StringUtils.isNotEmpty(feeJson)) {
            feeRelationMap = JSONObject.parseObject(compJson, Map.class);
        } else {
            feeRelationMap = feeRelationService.list().stream().collect(Collectors.toMap(CustomsFinanceFeeRelation::getYunbaoguanName, e -> e));
            redisUtils.set(yunbaoguanFeeKey, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        if (Objects.isNull(feeRelationMap) || Objects.isNull(coRelationMap)) {
            log.error("基础数据加载失败：费用或公司对应关系表加载失败");
            return false;
        }

        for (CustomsReceivable item : customsReceivable) {
            //要写入金蝶的实体
            ReceivableHeaderForm dataForm = new ReceivableHeaderForm();

            //拼装信息

            //写入费用项
            log.info("1.开始拼装应收明细...");
            List<APARDetailForm> list = new ArrayList<>();
            Class<? extends CustomsReceivable> clz = item.getClass();
            Field[] fields = clz.getDeclaredFields();
            StringBuilder errorString = new StringBuilder();
            for (Field field : fields) {
                String name = field.getName();

                //找到实体类中带isFee注解的数据
                if (Objects.nonNull(field.getDeclaredAnnotation(IsFee.class))) {
                    //读出注解@ApiModelProperty中的中文释义进行匹配
                    try {
                        Method getMethod = clz.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                        Object invoke = getMethod.invoke(item);
                        //费用项为零不记录
                        if (Objects.isNull(invoke) || Objects.equals("0.00", invoke.toString())) {
                            continue;
                        }
                        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                        CustomsFinanceFeeRelation customsFinanceFeeRelation = feeRelationMap.get(annotation.value());
                        if (Objects.isNull(customsFinanceFeeRelation)) {
                            //费用无法对应金蝶,记录
                            errorString.append(String.format("云报关费用项%s无法匹配金蝶费用项;", annotation.value()));
                            log.error(String.format("云报关费用项%s无法匹配金蝶费用项;", annotation.value()));
                        }
                        APARDetailForm feeItem = new APARDetailForm();
                        feeItem.setExpenseName(customsFinanceFeeRelation.getKingdeeName());
                        feeItem.setExpenseTypeName(customsFinanceFeeRelation.getType());
                        feeItem.setExpenseCategoryName(customsFinanceFeeRelation.getCategory());
                        feeItem.setPriceQty(BigDecimal.ONE);
                        //处理费用类别为代垫税金的数据
                        if ((Objects.equals(customsFinanceFeeRelation.getCategory(), "代垫税金"))) {
                            feeItem.setTaxRate(BigDecimal.ZERO);
                        } else {
                            feeItem.setTaxRate(new BigDecimal(customsFinanceFeeRelation.getTaxRate()));
                        }
                        feeItem.setTaxPrice(new BigDecimal(invoke.toString()));
                        list.add(feeItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


            if (CollectionUtil.isEmpty(list)) {
                log.info(String.format("应收费用项没有数据，退出方法"));
//                return CommonResult.success();
                return true;
            }
            dataForm.setEntityDetail(list);

            //抬头信息
            //尝试查询客戶名并写入
            log.info("2.开始拼装表单头部...");
            //从财务给的数据中对应到金蝶的客户名称
            CustomsFinanceCoRelation customsFinanceCoRelation = coRelationMap.get(item.getCustomerName());
            String kingdeeCompName = customsFinanceCoRelation == null ? "" : customsFinanceCoRelation.getKingdeeName();
            Optional<Customer> customer = baseService.get(kingdeeCompName, Customer.class);
            if (!customer.isPresent()) {
                log.error(String.format("没有找到对应的客户name='%s'", item.getCustomerName()));
                errorString.append(String.format("没有找到对应的客户name='%s'", item.getCustomerName()));
            }
            dataForm.setCustomerName(kingdeeCompName);
            dataForm.setCurrency("CNY");
            dataForm.setSettleOrgName("深圳市佳裕达报关有限公司");
            dataForm.setSaleDeptName("报关部");
            dataForm.setBusinessNo(item.getCustomApplyNo());
            dataForm.setRemark(getRemark());
            dataForm.setBillNo("");
            dataForm.setBaseCurrency("CNY");
            dataForm.setBusinessDate(item.getApplyDt());


            log.info("3.拼装完毕，程序汇总可能异常...");
            if (StringUtils.isNotBlank(errorString.toString())) {
                log.error(errorString.toString());
//                return CommonResult.error(ResultEnum.INTERNAL_SERVER_ERROR, errorString.toString());
                return false;
            }

            try {
                log.info("4.拼装校验无异常，开始向金蝶发送数据...");
                CommonResult commonResult = kingdeeService.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应收单推送完毕");
//                    return CommonResult.success("金蝶应付单推送完毕");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
//                return CommonResult.error(ResultEnum.INTERNAL_SERVER_ERROR, "保存失败");
                return false;
            }
        }
//        return CommonResult.error(ResultEnum.INTERNAL_SERVER_ERROR, "未知异常抛出");
        return false;
    }

    private boolean checkForDuplicateOrder(String applyNo) {
        List<InvoiceBase> existingReceivable = (List<InvoiceBase>) baseService.query(applyNo, Receivable.class);
        List<InvoiceBase> existingPayable = (List<InvoiceBase>) baseService.query(applyNo, Payable.class);
        if (CollectionUtil.isNotEmpty(existingReceivable) || CollectionUtil.isNotEmpty(existingPayable)) {
            if (CollectionUtil.isNotEmpty(existingReceivable)) {
                log.info("应收单已经存在，不能重复推送");
            }
            if (CollectionUtil.isNotEmpty(existingPayable)) {
                log.info("应收单已经存在，不能重复推送");
            }
            return true;
        }
        return false;
    }

    private String getRemark() {
        return String.format("报关应收单测试%s", String.valueOf(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 处理云报关的应收推送到金蝶接口
     * by william
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/yunbaoguan/payable/push", method = RequestMethod.POST)
    @ApiOperation(value = "接收云报关的应收单信息推至金蝶")
    public Boolean savePayableBill(@RequestBody Map<String, String> msg) {
        String reqMsg = MapUtil.getStr(msg, "msg");
        log.info("金蝶接收到报关应付数据：{}", reqMsg);
        //拼装根据入参拼装实体数据
        List<CustomsPayable> customsPayableForms = JSONArray.parseArray(reqMsg).toJavaList(CustomsPayable.class);

        //空数据校验
        if (CollectionUtil.isEmpty(customsPayableForms)) {
            log.info("应付费用项为空，退出方法");
//            return CommonResult.success();
            return true;
        } else {
            //非空时重单校验
            //获取报关单号
            Optional<CustomsPayable> first = customsPayableForms.stream().filter(Objects::isNull).findFirst();
            if (first.isPresent() && checkForDuplicateOrder(first.get().getCustomApplyNo())) {
                return true;
            }

            //基础校验完毕，装载预加载数据
            //基本校验完毕，装载预制数据
            Map<String, CustomsFinanceCoRelation> coRelationMap = null;
            Map<String, CustomsFinanceFeeRelation> feeRelationMap = null;
            String compJson = redisUtils.get(yunbaoguanCompKey);
            if (StringUtils.isNotEmpty(compJson)) {
                coRelationMap = JSONObject.parseObject(compJson, Map.class);
            } else {
                coRelationMap = coRelationService.list().stream().collect(Collectors.toMap(CustomsFinanceCoRelation::getYunbaoguanName, e -> e));
                redisUtils.set(yunbaoguanCompKey, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
            }
            String feeJson = redisUtils.get(yunbaoguanFeeKey);
            if (StringUtils.isNotEmpty(feeJson)) {
                feeRelationMap = JSONObject.parseObject(compJson, Map.class);
            } else {
                feeRelationMap = feeRelationService.list().stream().collect(Collectors.toMap(CustomsFinanceFeeRelation::getYunbaoguanName, e -> e));
                redisUtils.set(yunbaoguanFeeKey, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
            }
            if (Objects.isNull(feeRelationMap) || Objects.isNull(coRelationMap)) {
                log.error("基础数据加载失败：费用或公司对应关系表加载失败");
                return false;
            }


            //整理出不同的应付供应商，每一个应付供应商会生成一个单独的应付单
            log.info("开始根据供应商分组...");
            Map<String, List> diffSupplierMaps = new HashMap<>();
            for (CustomsPayable details : customsPayableForms) {

                String supplierName = details.getTargetName();
                CustomsFinanceFeeRelation feeRelation = feeRelationMap.get(details.getFeeName());
                //代垫税金项目单独列给海关
                if (Objects.nonNull(feeRelation)) {
                    if (feeRelation.getCategory().equals("代垫税金")) {
                        if (diffSupplierMaps.containsKey("海关")) {
                            diffSupplierMaps.get("海关").add(details);
                        } else {
                            diffSupplierMaps.put("海关", Lists.newArrayList(details));
                        }
                    } else {
                        if (diffSupplierMaps.containsKey(supplierName)) {
                            diffSupplierMaps.get(supplierName).add(details);
                        } else {
                            diffSupplierMaps.put(supplierName, Lists.newArrayList(details));
                        }
                    }
                }
            }

            //遍历供应商，将数据依次写入金蝶中
//        ExecutorService service = Executors.newCachedThreadPool();
            for (Map.Entry<String, List> stringListEntry : diffSupplierMaps.entrySet()) {
                List<CustomsPayable> customsPayableEntity = (List<CustomsPayable>) stringListEntry.getValue();
                //取出第一行获取相关的应付抬头信息
                CustomsPayable customsPayable = (CustomsPayable) customsPayableEntity.get(0);

                //要写入金蝶的数据
                PayableHeaderForm dataForm = new PayableHeaderForm();

                //声明异常日志
                StringBuilder errorString = new StringBuilder();


                dataForm.setBusinessNo(customsPayable.getCustomApplyNo());
                //尝试查询供应商名并写入
                CustomsFinanceCoRelation companyProfile = coRelationMap.get(customsPayable.getTargetName());
                String kingdeeCompName = companyProfile == null ? customsPayable.getTargetName() : companyProfile.getKingdeeName();
                Optional<Supplier> suppliers = baseService.get(kingdeeCompName, Supplier.class);
                if (!suppliers.isPresent()) {
                    log.info("传入的供应商名称 [" + customsPayable.getTargetName() + "] 无法找到相应的金蝶系统代码");
                    errorString.append("传入的供应商名称 [" + customsPayable.getTargetName() + "] 无法找到相应的金蝶系统代码");
                }

                //抬头信息
                dataForm.setSupplierName(kingdeeCompName);
                dataForm.setCurrency("CNY");
                dataForm.setSettleOrgName("深圳市佳裕达报关有限公司");
                dataForm.setRemark(getRemark());
                dataForm.setPurchaseDeptName("报关部");
                dataForm.setBillNo("");
                dataForm.setBaseCurrency("CNY");
                dataForm.setExchangeRate(BigDecimal.ONE);
                dataForm.setBusinessDate(customsPayable.getApplyDt());
                //费用明细
                List<APARDetailForm> list = new ArrayList<>();
                for (CustomsPayable payableForm : customsPayableEntity) {

                    CustomsFinanceFeeRelation customsFeeItem = feeRelationMap.get(payableForm.getFeeName());
                    if (Objects.isNull(customsFeeItem)) {
                        errorString.append(String.format("在金蝶的数据库中没有找到与%s相关的费用项", payableForm.getFeeName()));
                        continue;
                    }
                    APARDetailForm item = new APARDetailForm();
                    item.setExpenseName(customsFeeItem.getKingdeeName());
                    item.setExpenseCategoryName(customsFeeItem.getCategory());
                    item.setExpenseTypeName(customsFeeItem.getType());
                    item.setPriceQty(BigDecimal.ONE);
                    item.setTaxPrice(customsPayable.getCost());
                    item.setTaxRate(BigDecimal.ZERO);
                    list.add(item);
                }
                dataForm.setEntityDetail(list);

                if (StringUtils.isEmpty(errorString.toString())) {

                }

                //调用保存应付单接口
                try {
                    CommonResult commonResult = kingdeeService.savePayableBill(FormIDEnum.PAYABLE.getFormid(), dataForm);
                    if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                        log.info("金蝶应付单({})推送完毕", kingdeeCompName);
//                    return CommonResult.success("金蝶应付单推送完毕");
//                    return true;
                    }
//                return commonResult;
                } catch (Exception e) {
                    e.printStackTrace();
//                return CommonResult.error(ResultEnum.INTERNAL_SERVER_ERROR, "保存失败");
                }
            }

//        return CommonResult.error(ResultEnum.INTERNAL_SERVER_ERROR, "遇到其他错误抛出");
            return true;
        }

    }


}
