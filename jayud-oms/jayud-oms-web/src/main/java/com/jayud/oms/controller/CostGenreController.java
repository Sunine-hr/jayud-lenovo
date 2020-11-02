package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.AddCostGenreForm;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.service.ICostGenreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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
@Api(tags = "基础数据费用类型")
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
    public CommonResult enableOrDisableCostGenre(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.costGenreService.enableOrDisableCostGenre(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取费用类型详情,id是费用类型主键")
    @PostMapping(value = "/getCostGenreById")
    public CommonResult getCostGenreById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        CostGenreVO costInfo = this.costGenreService.getById(id);
        return CommonResult.success(costInfo);
    }

    @ApiModelProperty("查询所用启用费用类型")
    @PostMapping("/getEnableCostGenre")
    public CommonResult<List<CostGenre>> getEnableCostGenre() {
        List<CostGenre> costGenres = this.costGenreService.getEnableCostGenre();
        return CommonResult.success(costGenres);
    }
}

