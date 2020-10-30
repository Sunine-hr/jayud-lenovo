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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

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
@Api(tags = "费用类别列表接口")
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

    @ApiOperation(value = "更改启用/禁用费用类别状态,id是费用类别主键")
    @PostMapping(value = "/enableOrDisableCostType")
    public CommonResult enableOrDisableCostType(@RequestBody Map<String,Long> map) {
        Long id = map.get("id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        if (this.costTypeService.enableOrDisableCostType(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取费用类别详情,")
    @PostMapping(value = "/getCostTypeById")
    @Validated
    public CommonResult getCostTypeById(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        CostTypeVO costTypeVO = this.costTypeService.getById(id);
        return CommonResult.success(costTypeVO);
    }
}

