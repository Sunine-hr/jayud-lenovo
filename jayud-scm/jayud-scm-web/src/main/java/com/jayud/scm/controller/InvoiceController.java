package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.model.vo.InvoiceDetailVO;
import com.jayud.scm.service.IInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 结算单（应收款）主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/invoice")
@Api(tags = "应收款单管理")
public class InvoiceController {

    @Autowired
    private IInvoiceService iInvoiceService;

    @ApiOperation(value = "根据条件分页查询所有收款单信息")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<InvoiceDetailVO>> findByPage(@RequestBody QueryForm form) {
        IPage<InvoiceDetailVO> page = this.iInvoiceService.findByPage(form);

        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }




}

