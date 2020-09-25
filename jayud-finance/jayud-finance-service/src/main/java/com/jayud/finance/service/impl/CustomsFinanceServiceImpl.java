package com.jayud.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
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
import com.jayud.finance.kingdeesettings.K3CloudConfig;
import com.jayud.finance.po.*;
import com.jayud.finance.service.*;
import com.jayud.finance.util.KingdeeHttpUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomsFinanceServiceImpl implements CustomsFinanceService {
    @Autowired
    RedisUtils redisUtils;
    @Value("${relation-setting.redis-key.yunbaoguan.rec-company}")
    String yunbaoguanCompKeyRec;
    @Value("${relation-setting.redis-key.yunbaoguan.rec-fee}")
    String yunbaoguanFeeKeyRec;
    @Value("${relation-setting.redis-key.yunbaoguan.pay-company}")
    String yunbaoguanCompKeyPay;
    @Value("${relation-setting.redis-key.yunbaoguan.pay-fee}")
    String yunbaoguanFeeKeyPay;
    @Autowired
    KingdeeService kingdeeService;
    @Autowired
    BaseService baseService;

    @Autowired
    ICustomsFinanceCoRelationService coRelationService;
    @Autowired
    ICustomsFinanceFeeRelationService feeRelationService;
    @Autowired
    CookieService cookieService;
    @Autowired
    K3CloudConfig k3CloudConfig;

    @Override
    public Boolean pushReceivable(List<CustomsReceivable> customsReceivable) {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = new HashMap<>();
        String compJson = redisUtils.get(yunbaoguanCompKeyRec);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = (Map<String, CustomsFinanceCoRelation>) JSONObject.parseObject(compJson, Map.class);
        } else {
            List<CustomsFinanceCoRelation> list = coRelationService.list();
            for (CustomsFinanceCoRelation item : list) {
                if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    coRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanCompKeyRec, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        String feeJson = redisUtils.get(yunbaoguanFeeKeyRec);
        if (StringUtils.isNotEmpty(feeJson)) {
            feeRelationMap = (Map<String, CustomsFinanceFeeRelation>) JSONObject.parseObject(feeJson, Map.class);
        } else {
            List<CustomsFinanceFeeRelation> list = feeRelationService.list();
            for (CustomsFinanceFeeRelation item : list) {
                if (!feeRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    feeRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanFeeKeyRec, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        if (CollectionUtil.isEmpty(feeRelationMap) || CollectionUtil.isEmpty(coRelationMap)) {
            log.error("基础数据加载失败：费用或公司对应关系表加载失败");
            return false;
        }


        //对应收单列表依次处理
        StringBuilder errorString = new StringBuilder();
        for (CustomsReceivable item : customsReceivable) {
            //要写入金蝶的实体
            ReceivableHeaderForm dataForm = new ReceivableHeaderForm();

            //拼装信息

            //写入费用项
            log.info("1.开始拼装应收明细...");
            List<APARDetailForm> list = new ArrayList<>();
            Class<? extends CustomsReceivable> clz = item.getClass();
            Field[] fields = clz.getDeclaredFields();

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

                        String jsonStr = JSONUtil.toJsonStr(feeRelationMap.get(annotation.value()));
                        CustomsFinanceFeeRelation customsFinanceFeeRelation = JSONObject.parseObject(jsonStr, CustomsFinanceFeeRelation.class);

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
                log.info(String.format("当前应收费用项没有数据，退出方法"));
                errorString.append(String.format("当前应收费用项没有数据，退出方法"));
                continue;
            }
            dataForm.setEntityDetail(list);

            //抬头信息
            //尝试查询客戶名并写入
            log.info("2.开始拼装表单头部...");
            //从财务给的数据中对应到金蝶的客户名称
            CustomsFinanceCoRelation customsFinanceCoRelation = JSONObject.parseObject(JSONUtil.toJsonStr(coRelationMap.get(item.getCustomerName())), CustomsFinanceCoRelation.class);
            String kingdeeCompName = customsFinanceCoRelation == null ? "" : customsFinanceCoRelation.getKingdeeName();
            Optional<Customer> customer = baseService.get(kingdeeCompName, Customer.class);
            if (!customer.isPresent()) {
                log.error(String.format("没有找到对应的客户name='%s'", item.getCustomerName()));
                errorString.append(String.format("没有找到对应的客户name='%s'", item.getCustomerName()));
                continue;
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


            log.info("3.拼装完毕，正在汇总拼装时是否发生的异常...");
            if (StringUtils.isNotBlank(errorString.toString())) {
                log.error(errorString.toString());
                return false;
            }

            try {
                log.info("4.拼装校验无异常，开始向金蝶发送数据...");
                CommonResult commonResult = kingdeeService.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应收单推送完毕：（{}）", kingdeeCompName);
//                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        if (StringUtils.isNotEmpty(errorString.toString())) {
            log.error(String.format("写入应收单的时候出现异常：{}"), errorString.toString());
            return false;
        }
        log.info(String.format("所有的应收数据推送完毕"));
        return true;
    }

    @Override
    public Boolean pushPayable(List<CustomsPayable> customsPayableForms) {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = new HashMap<>();
        String compJson = redisUtils.get(yunbaoguanCompKeyPay);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = (Map<String, CustomsFinanceCoRelation>) JSONObject.parseObject(compJson, Map.class);
        } else {
            List<CustomsFinanceCoRelation> list = coRelationService.list();
            for (CustomsFinanceCoRelation item : list) {
                if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    coRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanCompKeyPay, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        String feeJson = redisUtils.get(yunbaoguanFeeKeyPay);
        if (StringUtils.isNotEmpty(feeJson)) {
            feeRelationMap = (Map<String, CustomsFinanceFeeRelation>) JSONObject.parseObject(feeJson, Map.class);
        } else {
            List<CustomsFinanceFeeRelation> list = feeRelationService.list();
            for (CustomsFinanceFeeRelation item : list) {
                if (!feeRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                    feeRelationMap.put(item.getYunbaoguanName(), item);
                }
            }
            redisUtils.set(yunbaoguanFeeKeyPay, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        }
        if (CollectionUtil.isEmpty(feeRelationMap) || CollectionUtil.isEmpty(coRelationMap)) {
            log.error("费用或公司名基础数据加载异常");
            return false;
        }

        //整理出不同的应付供应商，每一个应付供应商会生成一个单独的应付单
        log.info("开始根据供应商分组...");
        StringBuilder errorString = new StringBuilder();
        Map<String, List> diffSupplierMaps = new HashMap<>();
        for (CustomsPayable details : customsPayableForms) {
            String supplierName = details.getTargetName();
            CustomsFinanceFeeRelation feeRelation = JSONObject.parseObject(JSONUtil.toJsonStr(feeRelationMap.get(details.getFeeName())), CustomsFinanceFeeRelation.class);
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
        log.info("开始写入应付单数据...");
        //遍历供应商，将数据依次写入金蝶中
        for (Map.Entry<String, List> stringListEntry : diffSupplierMaps.entrySet()) {
            List<CustomsPayable> customsPayableEntity = (List<CustomsPayable>) stringListEntry.getValue();
            //取出第一行获取相关的应付抬头信息
            CustomsPayable customsPayable = (CustomsPayable) customsPayableEntity.get(0);

            //要写入金蝶的数据
            PayableHeaderForm dataForm = new PayableHeaderForm();

            dataForm.setBusinessNo(customsPayable.getCustomApplyNo());
            //尝试查询供应商名并写入
            CustomsFinanceCoRelation companyProfile = JSONObject.parseObject(JSONUtil.toJsonStr(coRelationMap.get(customsPayable.getTargetName())), CustomsFinanceCoRelation.class);
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

                CustomsFinanceFeeRelation customsFeeItem = JSONObject.parseObject(JSONUtil.toJsonStr(feeRelationMap.get(payableForm.getFeeName())), CustomsFinanceFeeRelation.class);
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

            //调用保存应付单接口
            try {
                CommonResult commonResult = kingdeeService.savePayableBill(FormIDEnum.PAYABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应付单推送完毕：({})", kingdeeCompName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        if (StringUtils.isNotEmpty(errorString.toString())) {
            log.error("应付数据推送出现异常：{}", errorString.toString());
        } else {
            log.info("所有的应付数据推送完毕");
        }
        return false;
    }


    /**
     * 删除指定的报关单号的财务数据
     *
     * @param applyNo
     * @return
     */
    @Override
    public Boolean removeSpecifiedInvoice(String applyNo) {
        //校验订单是否存在
        Boolean removedAP = true;
        Boolean removedAR = true;

        //处理应收
        List<InvoiceBase> existingReceivable = (List<InvoiceBase>) baseService.query(applyNo, Receivable.class);
        if (CollectionUtil.isNotEmpty(existingReceivable)) {
            log.info("存在应收单数据如下，即将安排删除...");
            for (InvoiceBase receiveble2Remove : existingReceivable) {
                log.info(receiveble2Remove.getFBillNo());
            }
            removedAR = doRemove(existingReceivable
                            .stream()
                            .map(InvoiceBase::getFBillNo)
                            .collect(Collectors.toList())
                    , "AR_receivable");
        }
        //处理应付
        List<InvoiceBase> existingPayable = (List<InvoiceBase>) baseService.query(applyNo, Payable.class);
        if (CollectionUtil.isNotEmpty(existingPayable)) {
            log.info("存在应付单数据如下，即将安排删除...");
            for (InvoiceBase payable2Remove : existingPayable) {
                log.info(payable2Remove.getFBillNo());
            }
            removedAP = doRemove((existingPayable
                            .stream()
                            .map(InvoiceBase::getFBillNo)
                            .collect(Collectors.toList()))
                    , "AP_Payable");
        }
        if (!removedAP || !removedAR) {
            log.error(String.format("报关单号%s删除失败", applyNo));
            return false;
        }
        return true;
    }



    /**
     * 实际处理删除应收单数据
     *
     * @param orderList
     * @return
     */
    private Boolean doRemove(List<String> orderList, String formId) {
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookieService.getCookie(k3CloudConfig));

        Map<String, Object> param = new HashMap<>();
        param.put("Numbers", orderList);
        //拼装数据
        String jsonString = JSONUtil.toJsonStr(param);
        JSONObject data = JSONObject.parseObject(jsonString);
        String content = buildParam(formId, data);

        //向金蝶发送请求
        String result = KingdeeHttpUtil.httpPost(k3CloudConfig.getDelete(), header, content);
        return KingdeeHttpUtil.ifSucceed(result);
    }

    /**
     * 拼装实际请求的json数据
     *
     * @param formId
     * @param data
     * @return
     */
    private String buildParam(String formId, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formid", formId);
        jsonObject.put("data", data);
        String result = JSONUtil.toJsonStr(jsonObject);
        log.info("请求内容：{}", result);
        return result;
    }


    /**
     * 拼装备注信息
     *
     * @return
     */
    private String getRemark() {
        return String.format("报关应收单测试%s", String.valueOf(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
    }
}

