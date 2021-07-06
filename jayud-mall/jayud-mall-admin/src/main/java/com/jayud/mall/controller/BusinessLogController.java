package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryBusinessLogForm;
import com.jayud.mall.model.vo.BusinessLogVO;
import com.jayud.mall.service.IBusinessLogService;
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

@Api(tags = "A062-admin-业务日志接口")
@ApiSort(value = 62)
@RestController
@RequestMapping("/businesslog")
public class BusinessLogController {

    @Autowired
    IBusinessLogService businessLogService;

    @ApiOperation(value = "分页查询-业务操作日志")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findBusinessLogByPage")
    public CommonResult<CommonPageResult<BusinessLogVO>> findBusinessLogByPage(@Valid @RequestBody QueryBusinessLogForm form) {
        IPage<BusinessLogVO> pageList = businessLogService.findBusinessLogByPage(form);
        CommonPageResult<BusinessLogVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }



}
