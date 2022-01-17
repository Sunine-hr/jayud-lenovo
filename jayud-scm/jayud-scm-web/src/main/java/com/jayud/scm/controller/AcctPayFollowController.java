package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.AcctPayFollowVO;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import com.jayud.scm.service.IAcctPayFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 付款跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/acctPayFollow")
@Api(tags = "付款单跟踪信息管理")
public class AcctPayFollowController {

    @Autowired
    private IAcctPayFollowService acctPayFollowService;

    @ApiOperation(value = "根据付款单id获取付款单操作日志")
    @PostMapping(value = "/findListByAcctReceiptId")
    public CommonResult<CommonPageResult<AcctPayFollowVO>> findListByAcctReceiptId(@RequestBody QueryCommonForm form) {
        IPage<AcctPayFollowVO> page = this.acctPayFollowService.findListByAcctPayId(form);
        CommonPageResult<AcctPayFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

