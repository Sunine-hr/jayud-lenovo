package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerGuaranteeForm;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerAgreementVO;
import com.jayud.scm.model.vo.CustomerGuaranteeVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.ICustomerGuaranteeService;
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
 * 担保合同 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/customerGuarantee")
@Api(tags = "担保合同")
public class CustomerGuaranteeController {

    @Autowired
    private ICustomerGuaranteeService customerGuaranteeService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询所有该客户的担保合同")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerGuaranteeVO> page = this.customerGuaranteeService.findByPage(form);
        for (CustomerGuaranteeVO record : page.getRecords()) {
            record.setGuaranteeTypeName(record.getGuaranteeType());
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户担保合同")
    @PostMapping(value = "/saveOrUpdateCustomerGuarantee")
    public CommonResult saveOrUpdateCustomerGuarantee(@RequestBody AddCustomerGuaranteeForm form) {
        boolean result = customerGuaranteeService.saveOrUpdateCustomerGuarantee(form);
        if(!result){
            return CommonResult.error(444,"新增客户担保合同失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取担保合同信息的详情")
    @PostMapping(value = "/getCustomerGuaranteeById")
    public CommonResult<CustomerGuaranteeVO> getCustomerGuaranteeById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerGuaranteeVO customerGuaranteeVO = customerGuaranteeService.getCustomerGuaranteeById(id);
        customerGuaranteeVO.setGuaranteeTypeName(customerGuaranteeVO.getGuaranteeType());
        return CommonResult.success(customerGuaranteeVO);
    }

}

