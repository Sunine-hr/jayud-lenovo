package com.jayud.finance.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.annotations.IsFee;
import com.jayud.finance.bo.APARDetailForm;
import com.jayud.finance.bo.PayableHeaderForm;
import com.jayud.finance.bo.ReceivableHeaderForm;
import com.jayud.finance.enums.CustomsCompanyEnum;
import com.jayud.finance.enums.CustomsFeeEnum;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.Customer;
import com.jayud.finance.po.CustomsPayable;
import com.jayud.finance.po.CustomsReceivable;
import com.jayud.finance.po.Supplier;
import com.jayud.finance.service.BaseService;
import com.jayud.finance.service.KingdeeService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

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
    KingdeeService service;
    @Autowired
    BaseService baseService;

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
        String reqMsg = MapUtil.getStr(msg, "msg");
        log.info("金蝶接收到报关应收数据：{}", reqMsg);

        //feign调用之前确保从列表中取出单行数据且非空，因此此处不需再校验
        List<CustomsReceivable> customsReceivable = JSONObject.parseArray(reqMsg, CustomsReceivable.class);

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
                if (Objects.nonNull(field.getAnnotation(IsFee.class))) {
                    //读出注解@ApiModelProperty中的中文释义进行匹配
                    try {
                        Method getMethod = clz.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                        Object invoke = getMethod.invoke(item);
                        //费用项为零不记录
                        if (Objects.isNull(invoke) || BigDecimal.ZERO.equals(new BigDecimal(invoke.toString()))) {
                            continue;
                        }
                        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                        CustomsFeeEnum customsFeeEnum = CustomsFeeEnum.getEnum(annotation.value());
                        if (Objects.isNull(customsFeeEnum)) {
                            //费用无法对应金蝶,记录
                            errorString.append(String.format("云报关费用项%s无法匹配金蝶费用项;", annotation.value()));
                            log.error(String.format("云报关费用项%s无法匹配金蝶费用项;", annotation.value()));
                        }
                        APARDetailForm feeItem = new APARDetailForm();
                        feeItem.setExpenseName(customsFeeEnum.getKingdee());
                        feeItem.setExpenseTypeName(customsFeeEnum.getType());
                        feeItem.setExpenseCategoryName(customsFeeEnum.getCategory());
                        feeItem.setPriceQty(BigDecimal.ONE);
                        feeItem.setTaxRate(BigDecimal.ZERO);
                        BigDecimal bigDecimal = new BigDecimal(invoke.toString());

                        feeItem.setTaxPrice(bigDecimal);
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
            CustomsCompanyEnum compEnum = CustomsCompanyEnum.getEnum(item.getCustomerName());
            String kingdeeCompName = compEnum == null ? "" : compEnum.getKingdee();
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
                CommonResult commonResult = service.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), dataForm);
                if (Objects.nonNull(commonResult) && commonResult.getCode() == ResultEnum.SUCCESS.getCode()) {
                    log.info("金蝶应付单推送完毕");
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
        if (CollectionUtil.isEmpty(customsPayableForms)) {
            log.info("应付费用项为空，退出方法");
//            return CommonResult.success();
            return true;
        }
        //整理出不同的应付供应商，每一个应付供应商会生成一个单独的应付单
        log.info("开始根据供应商分组...");
        Map<String, List> diffSupplierMaps = new HashMap<>();
        for (CustomsPayable details : customsPayableForms) {
            String supplierName = details.getTargetName();
            if (diffSupplierMaps.containsKey(supplierName)) {
                diffSupplierMaps.get(supplierName).add(details);
            } else {
                diffSupplierMaps.put(supplierName, Lists.newArrayList(details));
            }
        }

        //遍历供应商，将数据依次写入金蝶中
//        ExecutorService service = Executors.newCachedThreadPool();
        for (Map.Entry<String, List> stringListEntry : diffSupplierMaps.entrySet()) {
            List<CustomsPayable> customsPayableentity = (List<CustomsPayable>) stringListEntry.getValue();
            //取出第一行获取相关的应付抬头信息
            CustomsPayable customsPayable = (CustomsPayable) customsPayableentity.get(0);

            //要写入金蝶的数据
            PayableHeaderForm dataForm = new PayableHeaderForm();

            //声明异常日志
            StringBuilder errorString = new StringBuilder();


            dataForm.setBusinessNo(customsPayable.getCustomApplyNo());
            //尝试查询供应商名并写入
            String kingdeeCompName = CustomsCompanyEnum.getEnum(customsPayable.getTargetName()).getKingdee();
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
            for (CustomsPayable payableForm : customsPayableentity) {
                CustomsFeeEnum customsFeeEnum = CustomsFeeEnum.getEnum(payableForm.getFeeName());
                if (Objects.isNull(customsFeeEnum)) {
                    errorString.append(String.format("在金蝶的数据库中没有找到与%s相关的费用项", payableForm.getFeeName()));
                    continue;
                }
                APARDetailForm item = new APARDetailForm();
                item.setExpenseName(customsFeeEnum.getKingdee());
                item.setExpenseCategoryName(customsFeeEnum.getCategory());
                item.setExpenseTypeName(customsFeeEnum.getType());
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
                CommonResult commonResult = service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), dataForm);
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
