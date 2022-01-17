package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerTruckDriverForm;
import com.jayud.scm.model.bo.AddCustomerTruckPlaceForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.service.ICustomerTruckDriverService;
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
 * 运输公司车牌司机信息 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/customerTruckDriver")
@Api(tags = "运输公司车牌司机信息管理")
public class CustomerTruckDriverController {


    @Autowired
    private ICustomerTruckDriverService customerTruckDriverService;

    @ApiOperation(value = "根据条件分页查询所有车牌信息")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<CustomerTruckDriverVO>> findByPage(@RequestBody QueryForm form) {
        IPage<CustomerTruckDriverVO> page = this.customerTruckDriverService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增或修改车牌信息")
    @PostMapping(value = "/saveOrUpdateCustomerTruckDriver")
    public CommonResult saveOrUpdateCustomerTruckDriver(@RequestBody AddCustomerTruckDriverForm form) {
        boolean result = this.customerTruckDriverService.saveOrUpdateCustomerTruckDriver(form);
        if(!result){
            return CommonResult.error(444,"新增修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取车牌信息")
    @PostMapping(value = "/getCustomerTruckDriverById")
    public CommonResult<CustomerTruckDriverVO> getCustomerTruckDriverById(@RequestBody QueryForm form) {
        return CommonResult.success(this.customerTruckDriverService.getCustomerTruckDriverById(form.getId()));
    }
}

