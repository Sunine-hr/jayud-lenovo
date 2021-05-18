package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CreateOrderCaseForm;
import com.jayud.mall.model.vo.CaseVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ordercase")
@Api(tags = "C011-client-订单对应箱号信息接口")
@ApiSort(value = 11)
public class OrderCaseController {

    @Autowired
    IOrderCaseService orderCaseService;


    //批量添加箱号
    @ApiOperation(value = "批量添加箱号(根据总箱数添加)")
    @PostMapping("/createOrderCaseList")
    @ApiOperationSupport(order = 1)
    public CommonResult<CaseVO> createOrderCaseList(@Valid @RequestBody CreateOrderCaseForm form){
        Integer cartons = form.getCartons();// 总箱数
        BigDecimal weight = form.getWeight();// 每箱重量(KG)
        BigDecimal length = form.getLength();// 长(cm)
        BigDecimal width = form.getWidth();// 宽(cm)
        BigDecimal height = form.getHeight();// 高(cm)
        BigDecimal zore = new BigDecimal("0");
        if(cartons <= 0){
            return CommonResult.error(-1, "总箱数不能小于或等于零");
        }
        if(weight.compareTo(zore) == -1){
            return CommonResult.error(-1, "重量不能小于或等于零");
        }
        if(length.compareTo(zore) == -1){
            return CommonResult.error(-1, "长不能小于或等于零");
        }
        if(width.compareTo(zore) == -1){
            return CommonResult.error(-1, "宽不能小于或等于零");
        }
        if(height.compareTo(zore) == -1){
            return CommonResult.error(-1, "高不能小于或等于零");
        }

        //历史添加箱子list
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        if(CollUtil.isEmpty(orderCaseVOList)){
            orderCaseVOList = new ArrayList<>();
        }

        List<OrderCaseVO> orderCaseVOList1 = orderCaseService.createOrderCaseList(form);


        //fba箱号-生成规则
        String extensionNumber = form.getExtensionNumber();
        String beginNumber = form.getBeginNumber();
        Integer bNumber = Integer.valueOf(beginNumber);
        for (int i = 0; i<orderCaseVOList1.size(); i++){
            OrderCaseVO orderCaseVO = orderCaseVOList1.get(i);
            if(i != 0){
                bNumber = bNumber + 1;
            }
            //数字转字符串,前面自动补0的实现,补4位数的零
            String fabNo = extensionNumber+"U"+String.format("%0"+beginNumber.length()+"d", bNumber);
            orderCaseVO.setFabNo(fabNo);
        }

        orderCaseVOList.addAll(orderCaseVOList1);

        CaseVO caseVO = new CaseVO();
        //(客户预报)总重量 实际重
        BigDecimal totalAsnWeight = new BigDecimal("0");
        //客户预报总的材积重 材积重
        BigDecimal totalVolumeWeight = new BigDecimal("0");
        //客户预报总的收费重 收费重
        BigDecimal totalChargeWeight = new BigDecimal("0");
        //(客户预报)总体积
        BigDecimal totalAsnVolume = new BigDecimal("0");

        for (int i = 0; i<orderCaseVOList.size(); i++){
            BigDecimal asnWeight = orderCaseVOList.get(i).getAsnWeight();
            BigDecimal volumeWeight = orderCaseVOList.get(i).getVolumeWeight();
            BigDecimal chargeWeight = orderCaseVOList.get(i).getChargeWeight();
            BigDecimal asnVolume = orderCaseVOList.get(i).getAsnVolume();
            totalAsnWeight = totalAsnWeight.add(asnWeight);
            totalVolumeWeight = totalVolumeWeight.add(volumeWeight);
            totalChargeWeight = totalChargeWeight.add(chargeWeight);
            totalAsnVolume = totalAsnVolume.add(asnVolume);
        }
        caseVO.setTotalAsnWeight(totalAsnWeight);
        caseVO.setTotalVolumeWeight(totalVolumeWeight);
        caseVO.setTotalChargeWeight(totalChargeWeight);
        caseVO.setTotalAsnVolume(totalAsnVolume);
        caseVO.setTotalCase(orderCaseVOList.size());
        caseVO.setOrderCaseVOList(orderCaseVOList);
        return CommonResult.success(caseVO);
    }


}
