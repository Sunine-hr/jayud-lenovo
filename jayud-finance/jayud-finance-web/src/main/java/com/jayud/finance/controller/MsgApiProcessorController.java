package com.jayud.finance.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.*;
import com.jayud.finance.service.BaseService;
import com.jayud.finance.service.CustomsFinanceService;
import com.jayud.finance.service.PreloadService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RefreshScope
public class MsgApiProcessorController {

    @Autowired
    BaseService baseService;
    @Autowired
    PreloadService preloadService;
    @Autowired
    CustomsFinanceService customsFinanceService;
    @Value("${kingdee.settings.allow-delete-push}")
    Boolean allowDeletePush;

    /**
     * 刷新公司和费用项目缓存
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/yunbaoguan/info/flush")
    @ApiOperation(value = "刷新公司和费用项目缓存")
    public CommonResult flushInfo() {
        preloadService.refreshCompanyRelationMap();
        preloadService.refreshFeeRelationMap();
        return CommonResult.success();
    }

    @Deprecated
    @RequestMapping(path = "/yunbaoguan/invoice/remove", method = RequestMethod.POST)
    public Boolean removeInvoice(@RequestBody String msg) {
        return true;
    }

    /**
     * 处理云报关的应收推送到金蝶接口
     * by william
     *
     * @param msg
     * @return
     */
    @RequestMapping(path = "/yunbaoguan/receivable/push", method = RequestMethod.POST)
    @ApiOperation(value = "接收云报关的应收单信息推至金蝶")
    public Boolean saveReceivableBill(@RequestBody String msg) {
        /**
         * 根据财务要求，税率改为1%
         * 根据财务要求，应收单才记税率，应付单不记税率
         * 根据财务要求，应收单中如果遇到费用项目的类别为代垫税金，费用类型为代收代垫的，均进行标记且不记税率
         **/
        Map param = JSONObject.parseObject(msg, Map.class);
        String reqMsg = MapUtil.getStr(param, "msg");
        log.debug("金蝶接收到报关应收数据：{}", reqMsg);

        //feign调用之前确保从列表中取出单行数据且非空，因此此处不需再校验
        List<CustomsReceivable> customsReceivable = JSONObject.parseArray(reqMsg, CustomsReceivable.class);

        //重单校验,只要金蝶中有数据就不能再次推送
        Optional<CustomsReceivable> first = customsReceivable.stream().filter(Objects::nonNull).findFirst();
        String applyNo = "";
        if (first.isPresent()) {
            Map<FormIDEnum, List<String>> existingMap = checkForReceivableDuplicateOrder(first.get().getCustomApplyNo());
            if (CollectionUtil.isNotEmpty(existingMap)) {
                applyNo = first.get().getCustomApplyNo();
                if (allowDeletePush) {
                    log.info("应收单{}已经存在，但允许删单重推，尝试删除旧数据...", applyNo);
                    YunbaoguanPushProperties properties = new YunbaoguanPushProperties();
                    properties.setApplyNo(applyNo);
                    properties.setExistingOrders(existingMap);
                    properties = customsFinanceService.removeSpecifiedInvoice(properties);
                    if (!properties.ifAllowPush(FormIDEnum.RECEIVABLE)) {
                        log.warn("{}应收单所在状态已经无法删除", applyNo);
                        return true;
                    }

                    //处理需要比对的推送
                    log.info("正在处理报关单{}应收数据...", applyNo);
                    return customsFinanceService.pushReceivable(customsReceivable, properties);
                } else {
                    log.error("应收单{}已经存在，不能重复推送", applyNo);
                    return true;
                }
            } else {
                //基本校验完毕，调用方法进行处理，不需要比对，直接推送
                log.info("正在处理报关单{}应收数据...", applyNo);
                YunbaoguanPushProperties properties = new YunbaoguanPushProperties();
                properties.setApplyNo(applyNo);
                return customsFinanceService.pushReceivable(customsReceivable, properties);
            }
        } else {
            String grabError = String.format("应收单异常：第一条数据==>%s", first.toString());
            log.error(grabError);
            return false;
        }
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
    public Boolean savePayableBill(@RequestBody String msg) {

        String reqMsg = MapUtil.getStr(JSONObject.parseObject(msg, Map.class), "msg");
        log.debug("金蝶接收到报关应付数据：{}", reqMsg);
        //拼装根据入参拼装实体数据
        List<CustomsPayable> customsPayableForms = JSONArray.parseArray(reqMsg).toJavaList(CustomsPayable.class);

        //空数据校验
        if (CollectionUtil.isEmpty(customsPayableForms)) {
            log.error("应付费用项为空，退出方法");
//            return CommonResult.success();
            return true;
        } else {


            //非空时重单校验
            //获取报关单号
            String applyNo = "";
            Optional<CustomsPayable> first = customsPayableForms.stream().filter(Objects::nonNull).findFirst();
            if (first.isPresent()) {
                Map<FormIDEnum, List<String>> existingMap = checkForPayableDuplicateOrder(first.get().getCustomApplyNo());
                applyNo = first.get().getCustomApplyNo();
                if (CollectionUtil.isNotEmpty(checkForPayableDuplicateOrder(first.get().getCustomApplyNo()))) {
                    if (allowDeletePush) {
                        log.info("应付单{}已经存在，但可以删单重推，正在处理...", applyNo);
                        YunbaoguanPushProperties properties = new YunbaoguanPushProperties();
                        properties.setApplyNo(applyNo);
                        properties.setExistingOrders(existingMap);
                        properties = customsFinanceService.removeSpecifiedInvoice(properties);
                        if (!properties.ifAllowPush(FormIDEnum.PAYABLE)) {
                            log.warn("{}应付单所在状态已经无法删除", applyNo);
                            return true;
                        }
                        return customsFinanceService.pushPayable(customsPayableForms, properties);
                    } else {
                        log.error("应付单{}已经存在，不能重复推送", applyNo);
                        return true;
                    }
                } else {
                    //基础校验完毕,没有现存的订单，不用比对直接推送
                    log.info("正在处理报关单{}应付数据", applyNo);
                    YunbaoguanPushProperties properties = new YunbaoguanPushProperties();
                    properties.setApplyNo(applyNo);
                    return customsFinanceService.pushPayable(customsPayableForms, properties);
                }
            } else {
                String grabError = String.format("应收单异常：第一条数据==>%s", first.toString());
                log.error(grabError);
                return false;
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/yunbaoguan/receivable/delete")
    @ApiOperation(value = "删除应收单")
    public Boolean checkNRemoveReceivable(@RequestBody Map<String, String> param) {
        //注意！！此处务必要验证applyNo不为空，否则会因为模糊查询""而将所有数据删除
        String applyNo = MapUtil.getStr(param, "applyNo");
        if (applyNo.equals("")) {
            return true;
        }
        Map<FormIDEnum, List<String>> existingMap = checkForReceivableDuplicateOrder(applyNo);
        YunbaoguanPushProperties properties = new YunbaoguanPushProperties();
        properties.setApplyNo(applyNo);
        properties.setExistingOrders(existingMap);
        properties = customsFinanceService.removeSpecifiedInvoice(properties);
        Map<FormIDEnum, List<String>> unRemovableOrders = properties.getUnRemovableOrders();
        if (null != unRemovableOrders) {
            unRemovableOrders.entrySet()
                    .stream()
                    .filter(e -> {//类型中订单号不为空的才可以继续
                        return CollectionUtil.isNotEmpty(e.getValue());
                    })
                    .forEach(e -> {//遍历内容不为空的类型，打印错误信息
                        e.getValue().forEach(p -> {
                            log.error("报关单号{} 的 {} 类型业务订单号 {} 状态不可删除 ", applyNo, e.getKey(), e.getValue());
                        });
                    });
        }

        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/yunbaoguan/payable/delete")
    @ApiOperation(value = "删除应付单")
    public Boolean checkNRemovePayable(@RequestBody Map<String, String> param) {
        //注意！！此处务必要验证applyNo不为空，否则会因为模糊查询""而将所有数据删除
        String applyNo = MapUtil.getStr(param, "applyNo");
        if (applyNo.equals("")) {
            return true;
        }
        Map<FormIDEnum, List<String>> existingMap = checkForPayableDuplicateOrder(applyNo);
        YunbaoguanPushProperties properties = new YunbaoguanPushProperties();
        properties.setApplyNo(applyNo);
        properties.setExistingOrders(existingMap);
        properties = customsFinanceService.removeSpecifiedInvoice(properties);
        Map<FormIDEnum, List<String>> unRemovableOrders = properties.getUnRemovableOrders();
        if (null != unRemovableOrders) {
            unRemovableOrders.entrySet()
                    .stream()
                    .filter(e -> {//类型中订单号不为空的才可以继续
                        return CollectionUtil.isNotEmpty(e.getValue());
                    })
                    .forEach(e -> {//遍历内容不为空的类型，打印错误信息
                        e.getValue().forEach(p -> {
                            log.error("报关单号{} 的 {} 类型业务订单号 {} 状态不可删除 ", applyNo, e.getKey(), e.getValue());
                        });
                    });
        }
        return true;
    }


    /**
     * 校验应收订单存在
     * 返回已存在的订单
     *
     * @param applyNo
     * @return
     */
    private Map<FormIDEnum, List<String>> checkForReceivableDuplicateOrder(String applyNo) {
        List<String> existingReceivable = ((List<InvoiceBase>) baseService.query(applyNo, Receivable.class)).stream().map(InvoiceBase::getFBillNo).collect(Collectors.toList());
        List<String> existingReceivableOther = ((List<InvoiceBase>) baseService.query(applyNo, ReceivableOther.class)).stream().map(InvoiceBase::getFBillNo).collect(Collectors.toList());
        Map<FormIDEnum, List<String>> result = new HashMap<>();
        if (CollectionUtil.isNotEmpty(existingReceivable)) {
            result.put(FormIDEnum.RECEIVABLE, existingReceivable);
        }
        if (CollectionUtil.isNotEmpty(existingReceivableOther)) {
            result.put(FormIDEnum.RECEIVABLE_OTHER, existingReceivableOther);
        }
        return result;
    }

    /**
     * 校验应付订单存在
     * 返回已存在的订单
     *
     * @param applyNo
     * @return
     */
    private Map<FormIDEnum, List<String>> checkForPayableDuplicateOrder(String applyNo) {
        List<String> existingPayable = ((List<InvoiceBase>) baseService.query(applyNo, Payable.class)).stream().map(InvoiceBase::getFBillNo).collect(Collectors.toList());
        List<String> existingPayableOther = ((List<InvoiceBase>) baseService.query(applyNo, PayableOther.class)).stream().map(InvoiceBase::getFBillNo).collect(Collectors.toList());
        Map<FormIDEnum, List<String>> result = new HashMap<>();
        if (CollectionUtil.isNotEmpty(existingPayable)) {
            result.put(FormIDEnum.PAYABLE, existingPayable);
        }
        if (CollectionUtil.isNotEmpty(existingPayableOther)) {
            result.put(FormIDEnum.PAYABLE_OTHER, existingPayableOther);
        }
        return result;
    }


}
