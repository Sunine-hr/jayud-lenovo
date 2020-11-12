package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.AddDriverOrderPaymentCostForm;
import com.jayud.oms.model.bo.AddDriverOrderTransportForm;
import com.jayud.oms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.oms.model.enums.DriverOrderStatusEnum;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.DriverOrderInfo;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.CurrencyInfoVO;
import com.jayud.oms.model.vo.DriverInfoLinkVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.security.util.SecurityUtil;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信小程序
 */
@RestController
@RequestMapping("/miniApp")
@Api(tags = "微信小程序")
public class MiniAppController {

    @Autowired
    TmsClient tmsClient;
    @Autowired
    private IDriverOrderInfoService driverOrderInfoService;
    @Autowired
    private ICostInfoService costInfoService;
    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Autowired
    private IOrderPaymentCostService orderPaymentCostService;

    @PostMapping("/findDriverOrderTransportForm")
    @ApiOperation(value = "查看司机中港订单")
    public CommonResult findDriverOrderTransportForm(@Valid @RequestBody QueryDriverOrderTransportForm form) {

        form.setDriverId(Long.valueOf(SecurityUtil.getUserInfo()));
        String status = form.getStatus();
        if (DriverOrderStatusEnum.ALL.getCode().equals(form.getStatus())) {
            //查询所有订单
            status = null;
        }
        //查询司机接单表，根据前端传的状态,返回对应信息

        //根据状态查询司机接单信息，如果没有代表还有没有接单
        List<DriverOrderInfo> driverOrderInfos = this.driverOrderInfoService.getDriverOrderInfoByStatus(form.getDriverId(), status);

        List<Long> orderIds = driverOrderInfos.stream().map(DriverOrderInfo::getOrderId).collect(Collectors.toList());
        //组装订单
        form.assemblyOrder(orderIds);

        //查全部，要已经接单数据带过去

        //查询待接单,把已经接单订单带到排除id集合里面
        //查询
        //在首页，先查询司机接单表信息，看是否存在数据，把已经接单信息排除掉，剩下就是待接单信息，
        // 要传待接单状态，然后进行转换状态去查询
        return CommonResult.success(tmsClient.getDriverOrderTransport(form).getData());
    }

    @PostMapping("/confirmOrderReceiving")
    @ApiOperation(value = "司机确认接单")
    public CommonResult confirmOrderReceiving(@Valid @RequestBody AddDriverOrderTransportForm form) {
        DriverOrderInfo driverOrderInfo = new DriverOrderInfo()
                .setDriverId(Long.valueOf(SecurityUtil.getUserInfo()))
                .setOrderId(form.getOrderId())
                .setOrderNo(form.getOrderNo())
                .setStatus(DriverOrderStatusEnum.IN_TRANSIT.getCode());
        DriverOrderInfo tmp = driverOrderInfoService.getByOrderId(form.getOrderId());
        if (tmp != null) {
            return CommonResult.error(400, "该订单已确认过接单");
        }
        if (this.driverOrderInfoService.saveOrUpdateDriverOrder(driverOrderInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

    }

    @PostMapping("/driverDispatchList")
    @ApiOperation(value = "司机派车单 orderNo=订单编号")
    public CommonResult driverDispatchList(@RequestBody Map<String, Object> map) {
        String orderNo = MapUtil.getStr(map, "orderNo");
        ApiResult apiResult = this.tmsClient.dispatchList(orderNo);
        return CommonResult.success(apiResult.getData());
    }

    @PostMapping("/initEmploymentFeeBox")
    @ApiOperation(value = "录用费用下拉选项")
    public CommonResult initEmploymentFeeBox() {
        List<CostInfo> costInfos = this.costInfoService.getCostInfoByStatus(StatusEnum.ENABLE.getCode());
        List<InitComboxStrVO> boxOne = new ArrayList<>();
        for (CostInfo costInfo : costInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(costInfo.getIdCode());
            comboxStrVO.setName(costInfo.getName());
            boxOne.add(comboxStrVO);
        }

        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo();
        for (CurrencyInfoVO currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(currencyInfo.getExchangeRate());
            initComboxStrVOS.add(comboxStrVO);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("costInfos", boxOne);
        map.put("currencys", initComboxStrVOS);

        return CommonResult.success(map);
    }

    /**
     * 录用费用
     */
    @PostMapping("/doEmploymentFee")
    @ApiOperation(value = "录用费用操作")
    public CommonResult doEmploymentFee(@Valid @RequestBody AddDriverOrderPaymentCostForm form) {
        //查询是否确认接单
        if (this.driverOrderInfoService.getByOrderId(form.getOrderId()) == null) {
            return CommonResult.error(400, "该订单没有确认接单,无法进行录用操作");
        }

        //根据中港订单编号查询主订单
        ApiResult result = this.tmsClient.getDriverOrderTransportById(form.getOrderId());
        if (!result.isOk()) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        JSONObject json = JSONObject.parseObject(result.getData().toString());
        String mainOrderNo = json.getString("mainOrderNo");
        //获取司机供应商
        String driverId = SecurityUtil.getUserInfo();
        DriverInfoLinkVO driverInfoLink = this.driverInfoService.getDriverInfoLink(Long.parseLong(driverId));

        OrderPaymentCost paymentCost = new OrderPaymentCost()
                .setCostCode(form.getCostCode())
                .setAmount(form.getAmount())
                .setCurrencyCode(form.getCurrencyCode())
                .setFiles(StringUtils.getFileStr(form.getFileViews()))
                .setFileName(StringUtils.getFileNameStr(form.getFileViews()))
                .setMainOrderNo(mainOrderNo)
                .setCustomerCode(driverInfoLink.getSupplierCode())
                .setCustomerName(driverInfoLink.getSupplierName());

        this.orderPaymentCostService.save(paymentCost);

        return CommonResult.success();
    }
}
