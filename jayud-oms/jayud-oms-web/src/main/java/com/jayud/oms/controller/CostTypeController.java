package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCostTypeForm;
import com.jayud.oms.model.bo.QueryCostTypeForm;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.service.ICostTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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
@Api(tags = "基础数据费用类别列表接口")
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
        CostType costType = new CostType().setId(form.getId())
                .setCode(form.getCode()).setCodeName(form.getCodeName());
        if (this.costTypeService.checkUnique(costType)){
            return CommonResult.error(400, "名称或代码已经存在");
        }
        if (this.costTypeService.saveOrUpdateCostType(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "更改启用/禁用费用类别状态,id是费用类别主键")
    @PostMapping(value = "/enableOrDisableCostType")
    public CommonResult enableOrDisableCostType(@RequestBody Map<String,String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id =Long.parseLong(map.get("id"));
        if (this.costTypeService.enableOrDisableCostType(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取费用类别详情,")
    @PostMapping(value = "/getCostTypeById")
    public CommonResult getCostTypeById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id =Long.parseLong(map.get("id"));
        CostTypeVO costTypeVO = this.costTypeService.getById(id);
        return CommonResult.success(costTypeVO);
    }


    @ApiOperation(value = "查询所有启用费用类别")
    @PostMapping(value = "/getEnableCostType")
    public CommonResult<List<CostTypeVO>> getEnableCostType() {
        List<CostType> costTypes = this.costTypeService.getEnableCostType();
        List<CostTypeVO> list=new ArrayList<>();
        for (CostType costType : costTypes) {
            CostTypeVO costGenreVO = ConvertUtil.convert(costType, CostTypeVO.class);
            list.add(costGenreVO);
        }
        return CommonResult.success(list);
    }
}

