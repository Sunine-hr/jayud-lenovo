package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import com.jayud.scm.model.vo.InvoiceFollowVO;
import com.jayud.scm.service.IAcctReceiptFollowService;
import com.jayud.scm.service.IInvoiceFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 结算单（应收款）跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/invoiceFollow")
@Api(tags = "应收款跟踪记录管理")
public class InvoiceFollowController {

    @Autowired
    private IInvoiceFollowService iInvoiceFollowService;

    @ApiOperation(value = "根据应收款单id获取应收款单操作日志")
    @PostMapping(value = "/findListByInvoiceId")
    public CommonResult<CommonPageResult<InvoiceFollowVO>> findListByInvoiceId(@RequestBody QueryCommonForm form) {
        IPage<InvoiceFollowVO> page = this.iInvoiceFollowService.findListByInvoiceId(form);
        CommonPageResult<InvoiceFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }
}

