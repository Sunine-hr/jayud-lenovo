package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTransportForm;
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

}
