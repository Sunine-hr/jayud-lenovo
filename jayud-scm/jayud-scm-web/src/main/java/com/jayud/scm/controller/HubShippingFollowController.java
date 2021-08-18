package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCommodityFollowForm;
import com.jayud.scm.model.bo.AddHubShippingFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.model.vo.HubShippingFollowVO;
import com.jayud.scm.service.IHubShippingFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 出库单跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubShippingFollow")
@Api(tags = "出库操作记录管理")
public class HubShippingFollowController {

    @Autowired
    private IHubShippingFollowService hubShippingFollowService;

    @ApiOperation(value = "根据商品id获取商品操作日志")
    @PostMapping(value = "/findListByHubShippingId")
    public CommonResult findListByHubShippingId(@RequestBody QueryCommonForm form) {
        IPage<HubShippingFollowVO> page = this.hubShippingFollowService.findListByHubShippingId(form);
        CommonPageResult<HubShippingFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增操作信息")
    @PostMapping(value = "/addHubShippingFollow")
    public CommonResult addHubShippingFollow(@RequestBody AddHubShippingFollowForm followForm) {
        boolean result = hubShippingFollowService.addHubShippingFollow(followForm);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"跟踪信息添加失败");
    }

}

