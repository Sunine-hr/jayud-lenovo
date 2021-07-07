package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ServiceItemForm;
import com.jayud.mall.model.vo.ServiceItemVO;
import com.jayud.mall.service.IServiceItemService;
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

@Api(tags = "A065-admin-服务项目接口")
@ApiSort(value = 65)
@RestController
@RequestMapping("/serviceitem")
public class ServiceItemController {

    @Autowired
    IServiceItemService serviceItemService;

    @ApiOperation(value = "查询list-服务项目信息")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findServiceItem")
    public CommonResult<List<ServiceItemVO>> findServiceItem(@Valid @RequestBody ServiceItemForm form){
        List<ServiceItemVO> serviceItemList = serviceItemService.findServiceItem(form);
        return CommonResult.success(serviceItemList);
    }

}
