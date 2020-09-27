package com.jayud.oms.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.FileView;
import com.jayud.oms.model.vo.InputCostVO;
import com.jayud.oms.model.vo.LogisticsTrackVO;
import com.jayud.oms.service.ILogisticsTrackService;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/orderCommon")
@Api(tags = "订单通用接口")
public class OrderCommonController {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private ILogisticsTrackService logisticsTrackService;

    @ApiOperation(value = "录入费用")
    @PostMapping(value = "/saveOrUpdateCost")
    public CommonResult saveOrUpdateCost(@RequestBody InputCostForm form) {
        if (form == null || form.getMainOrderId() == null || form.getPaymentCostList() == null ||
            form.getReceivableCostList() == null || form.getReceivableCostList().size() == 0 ||
            form.getPaymentCostList().size() == 0) {
            return CommonResult.error(400,"参数不合法");
        }
        if("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())){
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            for (InputPaymentCostForm paymentCost : paymentCostForms) {
                if(paymentCost.getCustomerName() == null || "".equals(paymentCost.getCustomerName())
                || paymentCost.getCostCode() == null || "".equals(paymentCost.getCostCode())
                || paymentCost.getUnitPrice() == null || paymentCost.getNumber() == null
                || paymentCost.getCurrencyCode() == null || "".equals(paymentCost.getCurrencyCode())
                || paymentCost.getAmount() == null || paymentCost.getExchangeRate() == null
                || paymentCost.getChangeAmount() == null){
                    return CommonResult.error(400,"参数不合法");
                }
            }
            for (InputReceivableCostForm receivableCost : receivableCostForms) {
                if(receivableCost.getCustomerName() == null || "".equals(receivableCost.getCustomerName())
                        || receivableCost.getCustomerCode() == null || "".equals(receivableCost.getCustomerCode())
                        || receivableCost.getCostCode() == null || "".equals(receivableCost.getCostCode())
                        || receivableCost.getUnitPrice() == null || receivableCost.getNumber() == null
                        || receivableCost.getCurrencyCode() == null || "".equals(receivableCost.getCurrencyCode())
                        || receivableCost.getAmount() == null || receivableCost.getExchangeRate() == null
                        || receivableCost.getChangeAmount() == null){
                    return CommonResult.error(400,"参数不合法");
                }
            }
        }
        //1.需求为，提交审核按钮跟在每一条记录后面 2.暂存是保存所有未提交审核的数据  3.提交审核的数据不可编辑和删除
        boolean result = orderInfoService.saveOrUpdateCost(form);
        if(!result){
            return CommonResult.error(400,"调用失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "费用详情,id= 主订单ID")
    @PostMapping(value = "/getCostDetail")
    public CommonResult getCostDetail(@RequestBody GetCostDetailForm form) {
        InputCostVO inputCostVO = orderInfoService.getCostDetail(form);
        return CommonResult.success(inputCostVO);
    }



    @ApiOperation(value = "费用审核")
    @PostMapping(value = "/auditCost")
    public CommonResult auditCost(@RequestBody AuditCostForm form) {
        if(form.getStatus() == null || "".equals(form.getStatus()) || !("3".equals(form.getStatus()) ||
                "0".equals(form.getStatus())) || form.getPaymentCosts() == null ||
           form.getReceivableCosts() == null ){
            return CommonResult.error(400,"参数不合法");
        }
        if((form.getPaymentCosts().size() + form.getPaymentCosts().size()) == 0){
            return CommonResult.error(400,"参数不合法");
        }
        for(OrderPaymentCost paymentCost : form.getPaymentCosts()){
            if(paymentCost.getId() == null || "".equals(paymentCost.getId())){
                return CommonResult.error(400,"参数不合法");
            }
        }
        for(OrderReceivableCost receivableCost : form.getReceivableCosts()){
            if(receivableCost.getId() == null || "".equals(receivableCost.getId())){
                return CommonResult.error(400,"参数不合法");
            }
        }
        boolean result = orderInfoService.auditCost(form);
        if(!result){
            return CommonResult.error(400,"调用失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "反馈状态列表")
    @PostMapping(value = "/findReplyStatus")
    public CommonResult<List<LogisticsTrackVO>> findReplyStatus(@RequestBody QueryLogisticsTrackForm form) {
        List<LogisticsTrackVO> logisticsTrackVOS = logisticsTrackService.findReplyStatus(form);
        return CommonResult.success(logisticsTrackVOS);
    }

    @ApiOperation(value = "反馈状态确认")
    @PostMapping(value = "/confirmReplyStatus")
    public CommonResult confirmReplyStatus(@RequestBody LogisticsTrackForm form) {
        if(form == null || form.getLogisticsTrackForms() == null || form.getLogisticsTrackForms().size() == 0){
            return CommonResult.error(400,"参数不合法");
        }
        //处理附件
        for (LogisticsTrackVO logisticsTrack : form.getLogisticsTrackForms()) {
            List<FileView> fileViews = logisticsTrack.getFileViewList();
            StringBuilder sb = new StringBuilder();
            for (FileView fileView : fileViews) {
                sb.append(fileView.getRelativePath()).append(",");
            }
            if(!"".equals(String.valueOf(sb))) {
                logisticsTrack.setStatusPic(sb.substring(0, sb.length() - 1));
            }
        }
        List<LogisticsTrack> logisticsTracks = ConvertUtil.convertList(form.getLogisticsTrackForms(),LogisticsTrack.class);
        logisticsTrackService.saveOrUpdateBatch(logisticsTracks);
        return CommonResult.success();
    }

    //创建订单界面





}

