package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.ISupplierServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplierserve")
@Api(tags = "供应商服务接口")
public class SupplierServeController {

    @Autowired
    ISupplierServeService supplierServeService;

    @ApiOperation(value = "分页查询供应商服务")
    @PostMapping("/findSupplierServeByPage")
    public CommonResult<CommonPageResult<SupplierServeVO>> findSupplierServeByPage(@RequestBody QuerySupplierServeForm form) {
        IPage<SupplierServeVO> pageList = supplierServeService.findSupplierServeByPage(form);
        CommonPageResult<SupplierServeVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


}
