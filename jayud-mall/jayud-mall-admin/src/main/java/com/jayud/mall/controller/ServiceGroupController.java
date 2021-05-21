package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryServiceGroupForm;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.bo.ServiceGroupIdForm;
import com.jayud.mall.model.vo.ServiceGroupVO;
import com.jayud.mall.service.IServiceGroupService;
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

@Api(tags = "A025-admin-报价服务组接口")
@ApiSort(value = 25)
@RestController
@RequestMapping("/servicegroup")
public class ServiceGroupController {

    @Autowired
    IServiceGroupService serviceGroupService;


    @ApiOperation(value = "查询报价服务组List")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findServiceGroup")
    public CommonResult<List<ServiceGroupVO>> findServiceGroup(@RequestBody ServiceGroupForm form) {
        List<ServiceGroupVO> list = serviceGroupService.findServiceGroup(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "报价服务组分页查询")
    @ApiOperationSupport(order = 2)
    @PostMapping("/findServiceGroupByPage")
    public CommonResult<CommonPageResult<ServiceGroupVO>> findServiceGroupByPage(
            @RequestBody QueryServiceGroupForm form) {
        IPage<ServiceGroupVO> pageList = serviceGroupService.findServiceGroupByPage(form);
        CommonPageResult<ServiceGroupVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存报价服务组")
    @ApiOperationSupport(order = 3)
    @PostMapping("/saveServiceGroup")
    public CommonResult<ServiceGroupVO> saveServiceGroup(@Valid @RequestBody ServiceGroupForm form){
        ServiceGroupVO serviceGroupVO = serviceGroupService.saveServiceGroup(form);
        return CommonResult.success(serviceGroupVO);
    }

    @ApiOperation(value = "根据id查询，报价服务组")
    @ApiOperationSupport(order = 4)
    @PostMapping("/findServiceGroupById")
    public CommonResult<ServiceGroupVO> findServiceGroupById(@Valid @RequestBody ServiceGroupIdForm from){
        Long id = from.getId();
        ServiceGroupVO serviceGroupVO = serviceGroupService.findServiceGroupById(id);
        return CommonResult.success(serviceGroupVO);
    }


}
