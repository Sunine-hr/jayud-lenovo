package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCommodityFollowForm;
import com.jayud.scm.model.bo.AddCustomerFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CommodityFollowVO;
import com.jayud.scm.model.vo.CustomerFollowVO;
import com.jayud.scm.service.ICommodityFollowService;
import com.jayud.scm.service.ICustomerFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerFollow")
@Api(tags = "客户跟踪信息管理")
public class CustomerFollowController {

    @Autowired
    private ICustomerFollowService customerFollowService;

    @ApiOperation(value = "根据客户id获取客户跟踪日志")
    @PostMapping(value = "/findListByCustomerId")
    public CommonResult findListByCustomerId(@RequestBody QueryCommonForm form) {
        IPage<CustomerFollowVO> page = this.customerFollowService.findListByCustomerId(form);
        CommonPageResult<CustomerFollowVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }
}

