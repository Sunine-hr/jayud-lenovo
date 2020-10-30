package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.AddCostGenreForm;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.service.ICostGenreService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 基础数据费用类型 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/costGenre")
public class CostGenreController {

    @Autowired
    private ICostGenreService costGenreService;

    @ApiModelProperty("分页查询费用类型")
    @PostMapping("/findCostGenreByPage")
    public CommonResult<CommonPageResult<CostGenreVO>> findCostGenreByPage(@RequestBody QueryCostGenreForm form) {
        IPage<CostGenreVO> iPage = this.costGenreService.findCostGenreByPage(form);
        CommonPageResult<CostGenreVO> pageResult = new CommonPageResult<>(iPage);
        return CommonResult.success(pageResult);
    }

    @ApiOperation(value = "新增编辑费用类型")
    @PostMapping(value = "/saveOrUpdateCostGenre")
    public CommonResult saveOrUpdateCostGenre(@Valid @RequestBody AddCostGenreForm form) {
        if (this.costGenreService.saveOrUpdateCostGenre(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "更改启用/禁用费用类型状态,id是费用类型主键")
    @PostMapping(value = "/enableOrDisableCostGenre")
    public CommonResult enableOrDisableCostGenre(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }

        if (this.costGenreService.enableOrDisableCostGenre(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取费用类型详情,id是费用类型主键")
    @PostMapping(value = "/getCostGenreById")
    public CommonResult getCostGenreById(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        CostGenreVO costInfo = this.costGenreService.getById(id);
        return CommonResult.success(costInfo);
    }
}

