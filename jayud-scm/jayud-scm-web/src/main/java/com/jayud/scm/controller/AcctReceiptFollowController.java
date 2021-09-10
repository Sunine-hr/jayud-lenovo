package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.AccountBankBillFollowVO;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import com.jayud.scm.service.IAccountBankBillFollowService;
import com.jayud.scm.service.IAcctReceiptFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 收款单跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/acctReceiptFollow")
@Api(tags = "收款单跟踪记录管理")
public class AcctReceiptFollowController {

    @Autowired
    private IAcctReceiptFollowService acctReceiptFollowService;

    @ApiOperation(value = "根据收款单id获取收款单操作日志")
    @PostMapping(value = "/findListByAcctReceiptId")
    public CommonResult<CommonPageResult<AcctReceiptFollowVO>> findListByAcctReceiptId(@RequestBody QueryCommonForm form) {
        IPage<AcctReceiptFollowVO> page = this.acctReceiptFollowService.findListByAcctReceiptId(form);
        CommonPageResult<AcctReceiptFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

