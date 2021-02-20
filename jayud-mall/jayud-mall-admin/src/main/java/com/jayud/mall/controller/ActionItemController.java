package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ActionItemForm;
import com.jayud.mall.model.bo.ActionItemParaForm;
import com.jayud.mall.model.bo.ActionItemQueryForm;
import com.jayud.mall.model.vo.ActionItemVO;
import com.jayud.mall.service.IActionItemService;
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
@RequestMapping("/actionitem")
@Api(tags = "A022-admin-操作项接口")
@ApiSort(value = 22)
public class ActionItemController {

    @Autowired
    IActionItemService actionItemService;

    @ApiOperation(value = "查询操作项list")
    @PostMapping("/findActionItem")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<ActionItemVO>> findActionItem(@RequestBody ActionItemQueryForm form) {
        List<ActionItemVO> list = actionItemService.findActionItem(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "保存操作项")
    @PostMapping("/saveActionItem")
    @ApiOperationSupport(order = 2)
    public CommonResult<ActionItemVO> saveActionItem(@Valid @RequestBody ActionItemForm form){
        return actionItemService.saveActionItem(form);
    }

    @ApiOperation(value = "禁用操作项")
    @PostMapping("/disabledActionItem")
    @ApiOperationSupport(order = 3)
    public CommonResult<ActionItemVO> disabledActionItem(@Valid @RequestBody ActionItemParaForm form){
        Integer id = form.getId();
        return actionItemService.disabledActionItem(id);
    }


}
