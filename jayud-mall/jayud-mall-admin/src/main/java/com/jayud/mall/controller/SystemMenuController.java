package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.vo.SystemMenuVO;
import com.jayud.mall.service.ISystemMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mfc
 * @description:
 * @date 2020/10/24 13:34
 */
@RestController
@RequestMapping("/systemmenu")
@Api(tags = "S005-后台-菜单管理")
@ApiSort(value = 5)
public class SystemMenuController {

    @Autowired
    ISystemMenuService menuService;


    @ApiOperation(value = "菜单管理-查询所有菜单")
    @PostMapping(value = "/findAllMenuVO")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<SystemMenuVO>> findAllMenuVO(){
        List<SystemMenuVO> menuVOList = menuService.findAllMenuVO();
        return CommonResult.success(menuVOList);
    }



}
