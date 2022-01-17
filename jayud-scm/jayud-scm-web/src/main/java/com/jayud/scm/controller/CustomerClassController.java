package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.po.CustomerClass;
import com.jayud.scm.model.vo.CustomerClassVO;
import com.jayud.scm.service.ICustomerClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户财务编号表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerClass")
@Api(tags = "客户财务编码管理")
public class CustomerClassController {

    @Autowired
    private ICustomerClassService customerClassService;

    @ApiOperation(value = "获取客户已设置的客户类型")
    @PostMapping(value = "/getCustomerClassByByCustomerId")
    public CommonResult<List<CustomerClassVO>> getCustomerClassByByCustomerId(@RequestBody Map<String,Object> map) {
        Integer customerId = MapUtil.getInt(map, "customerId");
        List<CustomerClass> customerClassByCustomerId = customerClassService.getCustomerClassByCustomerId(customerId);
        List<CustomerClassVO> customerClassVOS = ConvertUtil.convertList(customerClassByCustomerId, CustomerClassVO.class);
        return CommonResult.success(customerClassVOS);
    }
}

