package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.AddCustomsQuestionnaireForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;
import com.jayud.oms.service.ICustomsQuestionnaireService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 海关调查问卷 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/customsQuestionnaire")
public class CustomsQuestionnaireController {

    @Autowired
    private ICustomsQuestionnaireService customsQuestionnaireService;


    @ApiOperation("创建/编辑调查问卷")
    @PostMapping(value = "/addOrUpdate")
    public CommonResult addOrUpdate(@RequestBody AddCustomsQuestionnaireForm form) {
        form.checkAdd();
        this.customsQuestionnaireService.addOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation("分页查询调查问卷")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<CustomsQuestionnaireVO>> findByPage(@RequestBody QueryCustomsQuestionnaireForm form) {
        IPage<CustomsQuestionnaireVO> list = this.customsQuestionnaireService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(list));
    }


    @ApiOperation("获取海关调查问卷详情")
    @PostMapping(value = "/getById")
    public CommonResult<CustomsQuestionnaire> getById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        CustomsQuestionnaire customsQuestionnaire = this.customsQuestionnaireService.getById(id);
        return CommonResult.success(customsQuestionnaire);
    }
}

