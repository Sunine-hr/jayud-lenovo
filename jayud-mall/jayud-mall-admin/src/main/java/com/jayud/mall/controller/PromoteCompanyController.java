package com.jayud.mall.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PromoteCompanyIdForm;
import com.jayud.mall.model.bo.QueryPromoteCompanyForm;
import com.jayud.mall.model.bo.SavePromoteCompanyForm;
import com.jayud.mall.model.vo.PromoteCompanyVO;
import com.jayud.mall.service.IPromoteCompanyService;
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
@RequestMapping("/promotepompany")
@Api(tags = "A056-admin-推广公司接口")
@ApiSort(value = 56)
public class PromoteCompanyController {

    @Autowired
    IPromoteCompanyService promoteCompanyService;



    @ApiOperation(value = "1.分页查询公司、渠道page")
    @PostMapping("/findPromoteCompanyByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<PromoteCompanyVO>> findPromoteCompanyByPage(@RequestBody QueryPromoteCompanyForm form) {
        IPage<PromoteCompanyVO> pageList = promoteCompanyService.findPromoteCompanyByPage(form);
        CommonPageResult<PromoteCompanyVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "2.保存：新增、编辑公司；增加、编辑渠道；")
    @PostMapping("/savePromoteCompany")
    @ApiOperationSupport(order = 2)
    public CommonResult savePromoteCompany(@Valid @RequestBody SavePromoteCompanyForm form){
        promoteCompanyService.savePromoteCompany(form);
        return CommonResult.success("保存成功");
    }

    @ApiOperation(value = "3.查询公司下面的所有渠道")
    @PostMapping("/findPromoteCompanyByParentId")
    @ApiOperationSupport(order = 3)
    public CommonResult<List<PromoteCompanyVO>> findPromoteCompanyByParentId(@Valid @RequestBody PromoteCompanyIdForm form){
        Integer parentId = form.getCompanyId();
        List<PromoteCompanyVO> promoteCompanyVOS = promoteCompanyService.findPromoteCompanyByParentId(parentId);
        return CommonResult.success(promoteCompanyVOS);
    }

    @ApiOperation(value = "4.查询推广公司")
    @PostMapping("/findPromoteCompanyByCompanyId")
    @ApiOperationSupport(order = 4)
    public CommonResult<PromoteCompanyVO> findPromoteCompanyByCompanyId(@Valid @RequestBody PromoteCompanyIdForm form){
        Integer companyId = form.getCompanyId();
        PromoteCompanyVO promoteCompanyVOS = promoteCompanyService.findPromoteCompanyByCompanyId(companyId);
        return CommonResult.success(promoteCompanyVOS);
    }

}
