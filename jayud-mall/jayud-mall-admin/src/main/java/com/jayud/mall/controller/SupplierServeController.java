package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.bo.SupplierServeForm;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.ISupplierServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplierserve")
@Api(tags = "S015-后台-供应商服务接口")
@ApiSort(value = 15)
public class SupplierServeController {

    @Autowired
    ISupplierServeService supplierServeService;

    @ApiOperation(value = "分页查询供应商服务")
    @PostMapping("/findSupplierServeByPage")
    @ApiModelProperty(position = 1)
    public CommonResult<CommonPageResult<SupplierServeVO>> findSupplierServeByPage(@RequestBody QuerySupplierServeForm form) {
        IPage<SupplierServeVO> pageList = supplierServeService.findSupplierServeByPage(form);
        CommonPageResult<SupplierServeVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存供应商服务")
    @PostMapping("/saveSupplierServe")
    @ApiModelProperty(position = 2)
    public CommonResult saveSupplierServe(@RequestBody SupplierServeForm form){
        return supplierServeService.saveSupplierServe(form);
    }


}
