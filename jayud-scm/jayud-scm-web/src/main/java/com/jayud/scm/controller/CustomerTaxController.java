package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.AddCustomerTaxForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.CustomerTaxVO;
import com.jayud.scm.service.ICustomerRelationerService;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ICustomerTaxService;
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
 * 客户开票资料 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerTax")
@Api(tags = "客户开票资料")
public class CustomerTaxController {

    @Autowired
    private ICustomerTaxService customerTaxService;

    @Autowired
    private ICustomerService customerService;

    @ApiOperation(value = "根据条件分页查询所有该客户的所有开票资料")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerTaxVO> page = this.customerTaxService.findByPage(form);
        for (CustomerTaxVO record : page.getRecords()) {
            if(customerService.getById(record.getCustomerId()).getTaxNo() != null){
                record.setTaxNo(customerService.getById(record.getCustomerId()).getTaxNo());
            }
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户开票资料")
    @PostMapping(value = "/saveOrUpdateCustomerTax")
    public CommonResult saveOrUpdateCustomerRelationer(@RequestBody AddCustomerTaxForm form) {
        boolean result = customerTaxService.saveOrUpdateCustomerTax(form);
        if(!result){
            return CommonResult.error(444,"新增客户开票资料失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改默认值")
    @PostMapping(value = "/modifyDefaultValues")
    public CommonResult modifyDefaultValues(@RequestBody AddCustomerTaxForm form) {
        boolean result = customerTaxService.modifyDefaultValues(form);
        if(!result){
            return CommonResult.error(444,"修改客户开票资料默认值失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取开票资料信息的详情")
    @PostMapping(value = "/getCustomerRelationerById")
    public CommonResult<CustomerTaxVO> getCustomerTaxById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerTaxVO customerTaxVO = customerTaxService.getCustomerTaxById(id);
        return CommonResult.success(customerTaxVO);
    }

}

