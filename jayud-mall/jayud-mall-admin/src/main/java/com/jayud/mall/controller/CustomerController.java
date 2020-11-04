package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryCustomerForm;
import com.jayud.mall.model.vo.CustomerVO;
import com.jayud.mall.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@Api(tags = "客户接口")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @ApiOperation(value = "分页查询客户")
    @PostMapping("/findCustomerByPage")
    public CommonResult<CommonPageResult<CustomerVO>> findCustomerByPage(@RequestBody QueryCustomerForm form) {
        IPage<CustomerVO> pageList = customerService.findCustomerByPage(form);
        CommonPageResult<CustomerVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }



}
