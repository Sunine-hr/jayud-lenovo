package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerAgreementForm;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerAgreementVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.ICustomerAgreementService;
import com.jayud.scm.service.ICustomerRelationerService;
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
 * 客户协议表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerAgreement")
@Api(tags = "客户协议管理")
public class CustomerAgreementController {

    @Autowired
    private ICustomerAgreementService customerAgreementService;

    @ApiOperation(value = "根据条件分页查询所有该客户的客户协议")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerAgreementVO> page = this.customerAgreementService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户协议")
    @PostMapping(value = "/saveOrUpdateCustomerAgreement")
    public CommonResult saveOrUpdateCustomerAgreement(@RequestBody AddCustomerAgreementForm form) {
        boolean result = customerAgreementService.saveOrUpdateCustomerAgreement(form);
        if(!result){
            return CommonResult.error(444,"新增客户协议失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取客户协议信息的详情")
    @PostMapping(value = "/getCustomerAgreementById")
    public CommonResult<CustomerAgreementVO> getCustomerAgreementById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerAgreementVO customerAgreementVO = customerAgreementService.getCustomerAgreementById(id);
        return CommonResult.success(customerAgreementVO);
    }

}

