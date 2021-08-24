package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CheckOrderFollowVO;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.service.ICheckOrderFollowService;
import com.jayud.scm.service.ICommodityFollowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 提验货单跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/checkOrderFollow")
public class CheckOrderFollowController {

    @Autowired
    private ICheckOrderFollowService checkOrderFollowService;

    @ApiOperation(value = "根据商品id获取商品操作日志")
    @PostMapping(value = "/findListByCommodityId")
    public CommonResult findListByCheckOrderId(@RequestBody QueryCommonForm form) {
        IPage<CheckOrderFollowVO> page = this.checkOrderFollowService.findListByCheckOrderId(form);
        CommonPageResult<CheckOrderFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }


}

