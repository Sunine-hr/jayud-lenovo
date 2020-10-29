package com.jayud.oms.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.service.ICostTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 费用类型 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/costType")
public class CostTypeController {

    @Autowired
    private ICostTypeService costTypeService;

    @ApiOperation(value = "查询费用类别列表")
    @PostMapping(value = "/findCostTypeByPage")
    public CommonResult<CommonPageResult<CostTypeVO>> findCostTypeByPage(@RequestBody QueryCostTypeForm form) {
        IPage<CostTypeVO> pageList = costTypeService.findCostTypeByPage(form);
        CommonPageResult<CostTypeVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增编辑费用类别")
    @PostMapping(value = "/saveOrUpdateCostType")
    public CommonResult saveOrUpdateCostType(@Valid @RequestBody AddCostTypeForm form) {
        if (this.costTypeService.saveOrUpdateCostType(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除费用类别")
    @PostMapping(value = "/deleteCostType")
    public CommonResult deleteCostType(@Valid @RequestBody DeleteForm form) {
        if (this.costTypeService.deleteByIds(form.getIds())) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取费用类别详情")
    @PostMapping(value = "/getById")
    @Validated
    public CommonResult getById(@RequestParam(value = "id") Long id) {
        if (ObjectUtil.isNull(id)){
            return CommonResult.error(500,"id is required");
        }
        CostTypeVO costTypeVO = this.costTypeService.getById(id);
        return CommonResult.success(costTypeVO);
    }
}

