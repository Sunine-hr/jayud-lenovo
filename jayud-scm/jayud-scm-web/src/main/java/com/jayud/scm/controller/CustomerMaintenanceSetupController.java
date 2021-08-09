package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerAddressForm;
import com.jayud.scm.model.bo.AddCustomerMaintenanceSetupForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerAddressVO;
import com.jayud.scm.model.vo.CustomerMaintenanceSetupVO;
import com.jayud.scm.service.ICustomerAddressService;
import com.jayud.scm.service.ICustomerMaintenanceSetupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 客户维护人表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-07
 */
@RestController
@RequestMapping("/customerMaintenanceSetup")
@Api(tags = "客户维护人设置")
public class CustomerMaintenanceSetupController {

    @Autowired
    private ICustomerMaintenanceSetupService customerMaintenanceSetupService;

    @ApiOperation(value = "根据条件分页查询所有该客户的所有维护人")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerMaintenanceSetupVO> page = this.customerMaintenanceSetupService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户维护人")
    @PostMapping(value = "/saveOrUpdateCustomerMaintenanceSetup")
    public CommonResult saveOrUpdateCustomerMaintenanceSetup(@RequestBody AddCustomerMaintenanceSetupForm form) {
        boolean result = customerMaintenanceSetupService.saveOrUpdateCustomerMaintenanceSetup(form);
        if(!result){
            return CommonResult.error(444,"新增客户维护人失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取维护人信息的详情")
    @PostMapping(value = "/getCustomerMaintenanceSetupById")
    public CommonResult<CustomerMaintenanceSetupVO> getCustomerMaintenanceSetupById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerMaintenanceSetupVO customerMaintenanceSetupVO = customerMaintenanceSetupService.getCustomerMaintenanceSetupById(id);
        return CommonResult.success(customerMaintenanceSetupVO);
    }

}

