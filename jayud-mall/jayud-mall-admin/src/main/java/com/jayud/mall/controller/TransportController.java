package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTransportForm;
import com.jayud.mall.model.bo.TransportForm;
import com.jayud.mall.model.bo.TransportParaForm;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.TransportVO;
import com.jayud.mall.service.ITransportService;
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
import java.util.List;

@RestController
@RequestMapping("/transport")
@Api(tags = "A055-admin-运输管理接口")
@ApiSort(value = 55)
public class TransportController {

    @Autowired
    ITransportService transportService;

    @ApiOperation(value = "运输管理分页查询")
    @PostMapping("/findTransportByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<TransportVO>> findTransportByPage(
            @RequestBody QueryTransportForm form) {
        IPage<TransportVO> pageList = transportService.findTransportByPage(form);
        CommonPageResult<TransportVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //拼车提货-创建展示运输单号、提货信息、送货信息
    @ApiOperation(value = "拼车提货-创建展示运输单号、提货信息、送货信息")
    @PostMapping("/createTransport")
    @ApiOperationSupport(order = 2)
    public CommonResult<TransportVO> createTransport(@RequestBody List<OrderPickVO> form){
        return transportService.createTransport(form);
    }

    //拼车提货-确认
    @ApiOperation(value = "拼车提货-确认")
    @PostMapping("/affirmTransport")
    @ApiOperationSupport(order = 3)
    public CommonResult<TransportVO> affirmTransport(@Valid @RequestBody TransportForm form){
        return transportService.affirmTransport(form);
    }

    //拼车提货-取消，不做。关闭前端页面。

    //运输管理-编辑(查询展示)
    @ApiOperation(value = "运输管理-编辑(查询展示)")
    @PostMapping("/findTransport")
    @ApiOperationSupport(order = 4)
    public CommonResult<TransportVO> findTransport(@Valid @RequestBody TransportParaForm form){
        return transportService.findTransport(form);
    }

    //运输管理-编辑-确认
    @ApiOperation(value = "运输管理-编辑-确认")
    @PostMapping("/editAffirmTransport")
    @ApiOperationSupport(order = 5)
    public CommonResult<TransportVO> editAffirmTransport(@Valid @RequestBody TransportForm from){
        return transportService.editAffirmTransport(from);
    }

    //运输管理-编辑-取消，不做。关闭前端页面。

    //运输管理-确认送达
    @ApiOperation(value = "运输管理-确认送达")
    @PostMapping("/confirmDelivery")
    @ApiOperationSupport(order = 6)
    public CommonResult confirmDelivery(@Valid @RequestBody TransportParaForm form){
        return transportService.confirmDelivery(form);
    }


    //提货信息，查看运输进度
    @ApiOperation(value = "提货信息，查看运输进度")
    @PostMapping("/findTransportById")
    @ApiOperationSupport(order = 7)
    public CommonResult<TransportVO> findTransportById(@Valid @RequestBody TransportParaForm form){
        Long id = form.getId();
        TransportVO transportVO = transportService.findTransportById(id);
        return CommonResult.success(transportVO);
    }



}
