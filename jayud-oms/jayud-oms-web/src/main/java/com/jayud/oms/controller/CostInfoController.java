package com.jayud.oms.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.DeleteForm;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.service.ICostInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 费用名描述 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/costInfo")
@Api(tags = "费用名列表接口")
public class CostInfoController {

    @Autowired
    private ICostInfoService costInfoService;

    @ApiOperation(value = "查询费用名称列表")
    @PostMapping(value = "/findCostInfoByPage")
    public CommonResult<CommonPageResult<CostInfoVO>> findCostInfoByPage(@RequestBody QueryCostInfoForm form) {
        IPage<CostInfoVO> pageList = costInfoService.findCostInfoByPage(form);
        CommonPageResult<CostInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增编辑费用名称")
    @PostMapping(value = "/saveOrUpdateCostInfo")
    public CommonResult saveOrUpdateCostInfo(@Valid @RequestBody AddCostInfoForm form) {
        if (this.costInfoService.saveOrUpdateCostInfo(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除费用名称")
    @PostMapping(value = "/deleteCostInfo")
    public CommonResult deleteCostInfo(@Valid @RequestBody DeleteForm form) {
        if (this.costInfoService.deleteByIds(form.getIds())) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取费用名称详情")
    @PostMapping(value = "/getById")
    public CommonResult getById(@RequestParam(value = "id") Long id) {
        if (ObjectUtil.isNull(id)){
            return CommonResult.error(500,"id is required");
        }
        CostInfoVO costInfoVO = this.costInfoService.getById(id);
        return CommonResult.success(costInfoVO);
    }
}

