package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerBankForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerBankVO;
import com.jayud.scm.service.ICustomerBankService;
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
 * 客户银行表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerBank")
@Api(tags = "银行资料")
public class CustomerBankController {

    @Autowired
    private ICustomerBankService customerBankService;

    @ApiOperation(value = "根据条件分页查询所有该客户的银行资料")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerBankVO> page = this.customerBankService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户银行资料")
    @PostMapping(value = "/saveOrUpdateCustomerBank")
    public CommonResult saveOrUpdateCustomerBank(@RequestBody AddCustomerBankForm form) {
        boolean result = customerBankService.saveOrUpdateCustomerBank(form);
        if(!result){
            return CommonResult.error(444,"新增客户银行资料失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改默认值")
    @PostMapping(value = "/modifyDefaultValues")
    public CommonResult modifyDefaultValues(@RequestBody AddCustomerBankForm form) {
        boolean result = customerBankService.modifyDefaultValues(form);
        if(!result){
            return CommonResult.error(444,"修改客户银行资料默认值失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取银行资料的详情")
    @PostMapping(value = "/getCustomerBankById")
    public CommonResult<CustomerBankVO> getCustomerBankById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerBankVO customerBankVO = customerBankService.getCustomerBankById(id);
        return CommonResult.success(customerBankVO);
    }
}

