package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.LegalPersonForm;
import com.jayud.mall.model.vo.LegalPersonVO;
import com.jayud.mall.service.ILegalPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/legalperson")
@Api(tags = "A045-admin-法人接口")
@ApiSort(value = 45)
public class LegalPersonController {

    @Autowired
    ILegalPersonService legalPersonService;


    @ApiOperation(value = "查询法人主体list")
    @PostMapping("/findLegalPerson")
    @ApiOperationSupport(order = 1)
    @Deprecated
    public CommonResult<List<LegalPersonVO>> findLegalPerson(@Valid @RequestBody LegalPersonForm form){
        return legalPersonService.findLegalPerson(form);
    }

}
