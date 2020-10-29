package com.jayud.oms.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.AddProductBizForm;
import com.jayud.oms.model.bo.DeleteForm;
import com.jayud.oms.model.bo.QueryProductBizForm;
import com.jayud.oms.model.vo.ProductBizVO;
import com.jayud.oms.service.IProductBizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 产品服务对应业务类型 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/productBiz")
@Api(tags = "业务类型列表接口")
public class ProductBizController {

    @Autowired
    private IProductBizService productBizService;

    @ApiOperation(value = "查询业务类型列表")
    @PostMapping(value = "/findProductBizByPage")
    public CommonResult<CommonPageResult<ProductBizVO>> findProductBizByPage(@RequestBody QueryProductBizForm form) {
        IPage<ProductBizVO> pageList = productBizService.findProductBizByPage(form);
        CommonPageResult<ProductBizVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增编辑业务类型")
    @PostMapping(value = "/saveOrUpdateProductBiz")
    public CommonResult saveOrUpdateProductBiz(@Valid @RequestBody AddProductBizForm form) {
        if (this.productBizService.saveOrUpdateProductBiz(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除业务类型")
    @PostMapping(value = "/deleteProductBiz")
    public CommonResult deleteProductBiz(@Valid @RequestBody DeleteForm form) {
        if (this.productBizService.deleteByIds(form.getIds())) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取业务类型详情")
    @PostMapping(value = "/getById")
    public CommonResult getById(@RequestParam(value = "id") Long id) {
        if (ObjectUtil.isNull(id)){
            return CommonResult.error(500,"id is required");
        }
        ProductBizVO productBizVO = this.productBizService.getById(id);
        return CommonResult.success(productBizVO);
    }
}

