package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.AccountBankBillFollowVO;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.service.IAccountBankBillFollowService;
import com.jayud.scm.service.ICommodityFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 水单跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@RestController
@RequestMapping("/accountBankBillFollow")
@Api(tags = "水单跟踪记录表管理")
public class AccountBankBillFollowController {

    @Autowired
    private IAccountBankBillFollowService accountBankBillFollowService;

    @ApiOperation(value = "根据水单id获取水单操作日志")
    @PostMapping(value = "/findListByAccountBankBillId")
    public CommonResult findListByAccountBankBillId(@RequestBody QueryCommonForm form) {
        IPage<AccountBankBillFollowVO> page = this.accountBankBillFollowService.findListByAccountBankBillId(form);
        CommonPageResult<AccountBankBillFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

