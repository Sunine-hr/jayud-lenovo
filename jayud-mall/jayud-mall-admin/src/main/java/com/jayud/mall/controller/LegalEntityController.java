package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.LegalEntityAuditForm;
import com.jayud.mall.model.bo.LegalEntityForm;
import com.jayud.mall.model.bo.LegalEntityIdForm;
import com.jayud.mall.model.bo.QueryLegalEntityForm;
import com.jayud.mall.model.vo.LegalEntityVO;
import com.jayud.mall.service.ILegalEntityService;
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

@Api(tags = "A064-admin-法人主体接口")
@ApiSort(value = 64)
@RestController
@RequestMapping("/legalentity")
public class LegalEntityController {

    @Autowired
    ILegalEntityService legalEntityService;


    @ApiOperation(value = "分页查询-法人主体信息")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findLegalEntityPage")
    public CommonResult<CommonPageResult<LegalEntityVO>> findLegalEntityPage(@Valid @RequestBody QueryLegalEntityForm form) {
        IPage<LegalEntityVO> pageList = legalEntityService.findLegalEntityPage(form);
        CommonPageResult<LegalEntityVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "根据id查询，法人主体信息")
    @ApiOperationSupport(order = 2)
    @PostMapping("/findLegalEntityById")
    public CommonResult<LegalEntityVO> findLegalEntityById(@Valid @RequestBody LegalEntityIdForm form){
        Long id = form.getId();
        LegalEntityVO legalEntityVO = legalEntityService.findLegalEntityById(id);
        return CommonResult.success(legalEntityVO);
    }

    @ApiOperation(value = "保存法人主体")
    @ApiOperationSupport(order = 3)
    @PostMapping("/saveLegalEntity")
    public CommonResult saveLegalEntity(@Valid @RequestBody LegalEntityForm form){
        legalEntityService.saveLegalEntity(form);
        return CommonResult.success("保存成功");
    }

    @ApiOperation(value = "审核，法人主体")
    @ApiOperationSupport(order = 4)
    @PostMapping("/auditLegalEntity")
    public CommonResult auditLegalEntity(@Valid @RequestBody LegalEntityAuditForm form){
        legalEntityService.auditLegalEntity(form);
        return CommonResult.success("审核成功");
    }

    @ApiOperation(value = "查询，法人主体list")
    @ApiOperationSupport(order = 4)
    @PostMapping("/findLegalEntity")
    public CommonResult<List<LegalEntityVO>> findLegalEntity(){
        List<LegalEntityVO> list = legalEntityService.findLegalEntity();
        return CommonResult.success(list);
    }

}
