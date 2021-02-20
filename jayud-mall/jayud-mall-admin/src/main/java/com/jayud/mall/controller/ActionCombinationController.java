package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ActionCombinationForm;
import com.jayud.mall.model.bo.ActionCombinationParaForm;
import com.jayud.mall.model.bo.ActionCombinationQueryForm;
import com.jayud.mall.model.vo.ActionCombinationVO;
import com.jayud.mall.service.IActionCombinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/actioncombination")
@Api(tags = "A023-admin-操作项组合接口")
@ApiSort(value = 23)
public class ActionCombinationController {

    @Autowired
    IActionCombinationService actionCombinationService;

    @ApiOperation(value = "查询操作项组合list")
    @PostMapping("/findActionCombination")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<ActionCombinationVO>> findActionCombination(@RequestBody ActionCombinationQueryForm form) {
        List<ActionCombinationVO> list = actionCombinationService.findActionCombination(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "保存操作项组合")
    @PostMapping("/saveActionCombination")
    @ApiOperationSupport(order = 2)
    public CommonResult<ActionCombinationVO> saveActionCombination(@Valid @RequestBody ActionCombinationForm form){
        return actionCombinationService.saveActionCombination(form);
    }

    @ApiOperation(value = "根据id查询操作项组合详情")
    @PostMapping("/findActionCombinationById")
    @ApiOperationSupport(order = 3)
    public CommonResult<ActionCombinationVO> findActionCombinationById(@Valid @RequestBody ActionCombinationParaForm form){
        Integer id = form.getId();
        return actionCombinationService.findActionCombinationById(id);
    }

    @ApiOperation(value = "停用-操作项组合详细接口")
    @PostMapping("/disabledActionCombination")
    @ApiOperationSupport(order = 4)
    public CommonResult<ActionCombinationVO> disabledActionCombination(@Valid @RequestBody ActionCombinationParaForm form){
        Integer id = form.getId();
        return actionCombinationService.disabledActionCombination(id);
    }


}
