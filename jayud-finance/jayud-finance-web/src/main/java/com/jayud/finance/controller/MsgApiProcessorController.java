package com.jayud.finance.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.CustomsFeeEnum;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.Customer;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

        //feign调用之前确保从列表中取出单行数据且非空，因此此处不需再校验
        CustomsReceivable customsReceivable = JSONObject.parseObject(reqMsg, CustomsReceivable.class);

        //要写入金蝶的实体
        ReceivableHeaderForm dataForm = new ReceivableHeaderForm();

        //拼装信息

        //抬头信息
        //尝试查询客戶名并写入
        Optional<Customer> suppliers = baseService.get(dataForm.getCustomerName(), Customer.class);
        if (suppliers.isPresent()) {
            log.info("传入的供应商名称 [" + dataForm.getCustomerName() + "] 无法找到相应的金蝶系统代码");
            dataForm.setCustomerName(suppliers.get().getFNumber());
        } else {
            dataForm.setCustomerName("盐田港国际资讯有限公司");
        }

        dataForm.setCurrency("CNY");
        dataForm.setSettleOrgName("深圳市佳裕达报关有限公司");
        dataForm.setSaleDeptName("报关部");
        dataForm.setBusinessNo(customsReceivable.getCustomApplyNo());
        dataForm.setRemark("报关应收单测试");
        dataForm.setBillNo("");
        dataForm.setBaseCurrency("CNY");
        dataForm.setBusinessDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        //写入费用项
        Field[] fields = customsReceivable.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            //找到实体类中带Cost字样的数据
            if (name.contains("Cost")) {

            }
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
    public void savePayableBill(@RequestBody Map<String, String> msg) {
        String reqMsg = MapUtil.getStr(msg, "msg");
        log.info("金蝶接收到报关应付数据：{}", reqMsg);
        //拼装根据入参拼装实体数据
        List<CustomsPayable> customsPayableForms = JSONArray.parseArray(reqMsg).toJavaList(CustomsPayable.class);

        //取出第一行获取相关的应付抬头信息
        CustomsPayable customsPayable = customsPayableForms.get(0);

        //要写入金蝶的数据
        PayableHeaderForm dataForm = new PayableHeaderForm();

        dataForm.setBusinessNo(customsPayable.getCustomApplyNo());
        //尝试查询供应商名并写入
        Optional<Supplier> suppliers = baseService.get(dataForm.getSupplierName(), Supplier.class);
        if (suppliers.isPresent()) {
            log.info("传入的供应商名称 [" + dataForm.getSupplierName() + "] 无法找到相应的金蝶系统代码");
            dataForm.setSupplierName(suppliers.get().getFNumber());
        }else{
            dataForm.setSupplierName("盐田港国际资讯有限公司");
        }
        //抬头信息
        dataForm.setCurrency("CNY");
        dataForm.setSettleOrgName("深圳市佳裕达报关有限公司");
        dataForm.setRemark("报关应付单测试20200919");
        dataForm.setPurchaseDeptName("报关部");
        dataForm.setBillNo("");
        dataForm.setBaseCurrency("CNY");
        dataForm.setExchangeRate(BigDecimal.ONE);
        dataForm.setBusinessDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //费用明细
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
        dataForm.setEntityDetail(list);

        //调用保存应付单接口
        service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), dataForm);
    }
}
