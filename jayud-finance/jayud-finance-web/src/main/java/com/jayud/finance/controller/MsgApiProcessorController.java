package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.CustomsFeeEnum;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.CustomsPayable;
import com.jayud.finance.po.CustomsReceivable;
import com.jayud.finance.po.Supplier;
import com.jayud.finance.service.BaseService;
import com.jayud.finance.service.KingdeeService;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void saveReceivableBill(@RequestBody Map<String, String> msg) {
        String reqMsg = MapUtil.getStr(msg, "msg");
        log.info("金蝶接收到报关应收数据：{}", reqMsg);
        List<CustomsReceivable> customsPayableForms = JSONArray.parseArray(reqMsg).toJavaList(CustomsReceivable.class);

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
    public void savePayableBill(@RequestBody Map<String, String> msg) {
        String reqMsg = MapUtil.getStr(msg, "msg");
        log.info("金蝶接收到报关应付数据：{}", reqMsg);
        //拼装数据
        List<CustomsPayable> customsPayableForms = JSONArray.parseArray(reqMsg).toJavaList(CustomsPayable.class);
        CustomsPayable customsPayable = customsPayableForms.get(0);
        //要写入金蝶的数据
        PayableHeaderForm payableHeaderForm = new PayableHeaderForm();

        payableHeaderForm.setBusinessNo(customsPayable.getCustomApplyNo());
        Optional<Supplier> suppliers = baseService.get(payableHeaderForm.getSupplierName(), Supplier.class);
        if (suppliers.isPresent()) {
            log.info("传入的供应商名称 [" + payableHeaderForm.getSupplierName() + "] 无法找到相应的金蝶系统代码");
            payableHeaderForm.setSupplierName(suppliers.get().getFNumber());
        }else{
            payableHeaderForm.setSupplierName("盐田港国际资讯有限公司");
        }


        payableHeaderForm.setCurrency("CNY");
        payableHeaderForm.setSettleOrgName("深圳市佳裕达报关有限公司");
        payableHeaderForm.setRemark("报关应付单测试20200919");
        payableHeaderForm.setPurchaseDeptName("报关部");
        payableHeaderForm.setBillNo("");
        payableHeaderForm.setBaseCurrency("CNY");
        payableHeaderForm.setExchangeRate(BigDecimal.ONE);

        List<APARDetailForm> list = new ArrayList<>();
        for (CustomsPayable payableForm : customsPayableForms) {
            CustomsFeeEnum customsFeeEnum = CustomsFeeEnum.getEnum(payableForm.getFeeName());
            if (Objects.isNull(customsFeeEnum)) {
                continue;
            }
            APARDetailForm item = new APARDetailForm();
            item.setExpenseName(customsFeeEnum.getKingdee());
            item.setExpenseCategoryName(customsFeeEnum.getCategory());
            item.setExpenseTypeName(customsFeeEnum.getType());
            item.setPriceQty(BigDecimal.ONE);
            item.setTaxPrice(BigDecimal.ZERO);
            item.setTaxRate(BigDecimal.ZERO);
            list.add(item);
        }
        payableHeaderForm.setEntityDetail(list);

       service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), payableHeaderForm);
    }
}
