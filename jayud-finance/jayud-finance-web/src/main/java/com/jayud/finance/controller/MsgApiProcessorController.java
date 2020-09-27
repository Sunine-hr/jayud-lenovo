package com.jayud.finance.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayud.finance.po.*;
import com.jayud.finance.service.BaseService;
import com.jayud.finance.service.CustomsFinanceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    BaseService baseService;
    @Autowired
    CustomsFinanceService customsFinanceService;
    @Value("${kingdee.settings.allow-delete-push}")
    Boolean allowDeletePush;

    @RequestMapping(path = "/yunbaoguan/invoice/remove", method = RequestMethod.POST)
    public Boolean removeInvoice(@RequestBody Map<String, String> param) {
        String applyNo = MapUtil.getStr(param, "applyNo");
        if (StringUtils.isEmpty(applyNo)) {
            return false;
        }
        return customsFinanceService.removeSpecifiedInvoice(applyNo);
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
        Optional<CustomsReceivable> first = customsReceivable.stream().filter(Objects::nonNull).findFirst();

        if (first.isPresent() && checkForReceivableDuplicateOrder(first.get().getCustomApplyNo())) {
            if (allowDeletePush) {
                customsFinanceService.removeSpecifiedInvoice(first.get().getCustomApplyNo());
            } else {
                return true;
            }
        }

        //基本校验完毕，调用方法进行处理
        return customsFinanceService.pushReceivable(customsReceivable);

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
            Optional<CustomsPayable> first = customsPayableForms.stream().filter(Objects::nonNull).findFirst();
            if (first.isPresent() && checkForPayableDuplicateOrder(first.get().getCustomApplyNo())) {
                if (allowDeletePush) {
                    customsFinanceService.removeSpecifiedInvoice(first.get().getCustomApplyNo());
                } else {
                    return true;
                }
            }
            //基础校验完毕
            return customsFinanceService.pushPayable(customsPayableForms);
        }
    }


    /**
     * 校验应收订单存在
     *
     * @param applyNo
     * @return
     */
    private boolean checkForReceivableDuplicateOrder(String applyNo) {
        List<InvoiceBase> existingReceivable = (List<InvoiceBase>) baseService.query(applyNo, Receivable.class);
        if (CollectionUtil.isNotEmpty(existingReceivable)) {
            log.info("应收单已经存在，不能重复推送");
            return true;
        }
        return false;
    }

    /**
     * 校验应付订单存在
     *
     * @param applyNo
     * @return
     */
    private boolean checkForPayableDuplicateOrder(String applyNo) {
        List<InvoiceBase> existingPayable = (List<InvoiceBase>) baseService.query(applyNo, Payable.class);
        if (CollectionUtil.isNotEmpty(existingPayable)) {
            log.info("应付单已经存在，不能重复推送");
            return true;
        }
        return false;
    }


}
