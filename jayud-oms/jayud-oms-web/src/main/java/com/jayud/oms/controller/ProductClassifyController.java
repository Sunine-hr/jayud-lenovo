package com.jayud.oms.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.ProductClassify;
import com.jayud.oms.model.vo.ProductClassifyVO;
import com.jayud.oms.service.IProductClassifyService;
import com.jayud.oms.service.ISupplierInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品分类 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-10-29
 */
@RestController
@RequestMapping("/productClassify")
public class ProductClassifyController {

    @Autowired
    private IProductClassifyService productClassifyService;

    @ApiOperation(value = "查询服务类型")
    @PostMapping(value = "/getEnableParentProductClassify")
    public CommonResult<List<ProductClassifyVO>> getEnableParentProductClassify(){
        List<ProductClassify> list = this.productClassifyService.getEnableParentProductClassify(StatusEnum.ENABLE.getCode());
        List<ProductClassifyVO> result=new ArrayList<>();
        list.forEach(tmp-> result.add(ConvertUtil.convert(tmp,ProductClassifyVO.class)));
        return CommonResult.success(result);
    }
}
