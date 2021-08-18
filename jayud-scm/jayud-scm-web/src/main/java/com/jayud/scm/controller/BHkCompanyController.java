package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.po.BHkCompany;
import com.jayud.scm.service.IBHkCompanyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 香港抬头表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/bHkCompany")
public class BHkCompanyController {

    @Autowired
    IBHkCompanyService bHkCompanyService;//香港抬头表

    @ApiOperation(value = "查询香港抬头list")
    @PostMapping(value = "/findBHkCompany")
    public CommonResult<List<BHkCompany>> findBHkCompany(){
        List<BHkCompany> bHkCompanieList = bHkCompanyService.findBHkCompany();
        return CommonResult.success(bHkCompanieList);
    }

}

