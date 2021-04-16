package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddCustomsQuestionnaireForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.enums.AuditStatusEnum;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.po.AuditInfo;
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


    @ApiOperation("审核操作")
    @PostMapping(value = "/auditOperation")
    public CommonResult<CustomsQuestionnaire> auditOperation(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        Integer status = MapUtil.getInt(map, "status");
        if (id == null || status == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        switch (status) {
            case 0:
                status = 1;
                break;
            case 1:
                status = 2;
        }
        this.customsQuestionnaireService.updateById(new CustomsQuestionnaire().setId(id).setStatus(status));
        return CommonResult.success();
    }

    @ApiOperation("审核驳回操作")
    @PostMapping(value = "/approvalRejection")
    public CommonResult approvalRejection(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        String auditOpinion = MapUtil.getStr(map, "auditOpinion");

        AuditInfo auditInfo = new AuditInfo().setExtId(id)
                .setExtDesc(AuditTypeDescEnum.TWO.getTable())
                .setAuditTypeDesc(AuditTypeDescEnum.TWO.getDesc())
                .setAuditStatus("reject")
                .setAuditComment(auditOpinion);
        this.customsQuestionnaireService.approvalRejection(auditInfo);
        return CommonResult.success();
    }
}

