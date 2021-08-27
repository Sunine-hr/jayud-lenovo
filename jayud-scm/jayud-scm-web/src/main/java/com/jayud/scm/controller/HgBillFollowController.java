package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.HgBillFollowVO;
import com.jayud.scm.model.vo.HgBillVO;
import com.jayud.scm.model.vo.HubShippingFollowVO;
import com.jayud.scm.service.IHgBillFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 报关单跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/hgBillFollow")
@Api(tags = "报关单跟踪信息")
public class HgBillFollowController {

    @Autowired
    private IHgBillFollowService hgBillFollowService;

    @ApiOperation(value = "根据报关单id获取报关操作日志")
    @PostMapping(value = "/findListByHgBillId")
    public CommonResult findListByHgBillId(@RequestBody QueryCommonForm form) {
        IPage<HgBillFollowVO> page = this.hgBillFollowService.findListByHgBillId(form);
        CommonPageResult<HgBillFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

