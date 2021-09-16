package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddOtherCostForm;
import com.jayud.scm.model.bo.AddVerificationReocrdsForm;
import com.jayud.scm.service.IOtherCostService;
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
 * 其他费用记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@RestController
@RequestMapping("/otherCost")
@Api(tags = "其他费用管理")
public class OtherCostController {

    @Autowired
    private IOtherCostService otherCostService;

    @ApiOperation(value = "新增其他费用")
    @PostMapping(value = "/saveOrUpdateOtherCost")
    public CommonResult saveOrUpdateOtherCost(@RequestBody AddOtherCostForm form){
        boolean result = otherCostService.saveOrUpdateOtherCost(form);
        if(!result){
            return CommonResult.error(444,"新增或修改失败");
        }
        return CommonResult.success();
    }
}

