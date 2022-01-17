package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerTruckPlaceForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.bo.QueryCustomerForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckDriver;
import com.jayud.scm.model.po.CustomerTruckPlace;
import com.jayud.scm.model.vo.CustomerBankVO;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ICustomerTruckDriverService;
import com.jayud.scm.service.ICustomerTruckPlaceService;
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
 * 运输公司车牌信息 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/customerTruckPlace")
@Api(tags = "运输公司车牌管理")
public class CustomerTruckPlaceController {

    @Autowired
    private ICustomerTruckPlaceService customerTruckPlaceService;

    @Autowired
    private ICustomerTruckDriverService customerTruckDriverService;

    @Autowired
    private ICustomerService customerService;

    @ApiOperation(value = "根据条件分页查询所有车牌信息")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<CustomerTruckPlaceVO>> findByPage(@RequestBody QueryForm form) {
        IPage<CustomerTruckPlaceVO> page = this.customerTruckPlaceService.findByPage(form);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            for (CustomerTruckPlaceVO record : page.getRecords()) {
                record.setCustomerName(customerService.getCustomerById(record.getCustomerId()).getCustomerName());
                List<CustomerTruckDriverVO> customerTruckDriverVOS = customerTruckDriverService.getTruckDriverByTruckId(record.getId());
                record.setCustomerTruckDriverVOS(customerTruckDriverVOS);
            }
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增或修改车牌信息")
    @PostMapping(value = "/saveOrUpdateCustomerTruckPlace")
    public CommonResult saveOrUpdateCustomerTruckPlace(@RequestBody AddCustomerTruckPlaceForm form) {
        boolean result = this.customerTruckPlaceService.saveOrUpdateCustomerTruckPlace(form);
        if(!result){
            return CommonResult.error(444,"新增修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取车牌信息")
    @PostMapping(value = "/getCustomerTruckPlaceById")
    public CommonResult<CustomerTruckPlaceVO> getCustomerTruckPlaceById(@RequestBody QueryForm form) {
        return CommonResult.success(this.customerTruckPlaceService.getCustomerTruckPlaceById(form.getId()));
    }
}

