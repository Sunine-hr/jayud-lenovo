package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddBusinessDevEvaluationForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.BusinessDevEvaluation;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.jayud.oms.model.vo.BusinessDevEvaluationVO;
import com.jayud.oms.service.IBusinessDevEvaluationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 商业伙伴开发评估表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/businessDevEvaluation")
public class BusinessDevEvaluationController {
    @Autowired
    private IBusinessDevEvaluationService businessDevEvaluationService;

    @ApiOperation("创建/编辑调查问卷")
    @PostMapping(value = "/addOrUpdate")
    public CommonResult addOrUpdate(@RequestBody AddBusinessDevEvaluationForm form) {
        form.checkAdd();
        form.setStatus(0);
        this.businessDevEvaluationService.addOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation("分页查询商业伙伴开发评估表")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<BusinessDevEvaluationVO>> findByPage(@RequestBody QueryCustomsQuestionnaireForm form) {
        IPage<BusinessDevEvaluationVO> list = this.businessDevEvaluationService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(list));
    }


    @ApiOperation("获取商业伙伴开发评估表详情")
    @PostMapping(value = "/getById")
    public CommonResult<BusinessDevEvaluation> getById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        BusinessDevEvaluation businessDevEvaluation = this.businessDevEvaluationService.getById(id);
        return CommonResult.success(businessDevEvaluation);
    }


    @ApiOperation("审核操作")
    @PostMapping(value = "/auditOperation")
    public CommonResult<CustomsQuestionnaire> auditOperation(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        Integer status = MapUtil.getInt(map, "status");
        String auditOpinion = MapUtil.getStr(map, "auditOpinion");
        if (id == null || status == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        BusinessDevEvaluation businessDevEvaluation = new BusinessDevEvaluation().setId(id);
        switch (status) {
            case 0:
                businessDevEvaluation.setStatus(1);
                businessDevEvaluation.setAuditOpinion(auditOpinion);
                break;
            case 1:
                businessDevEvaluation.setStatus(2);
                businessDevEvaluation.setEvaluationOpinion(auditOpinion);
        }
        this.businessDevEvaluationService.updateById(businessDevEvaluation);
        return CommonResult.success();
    }

    @ApiOperation("审核驳回操作")
    @PostMapping(value = "/approvalRejection")
    public CommonResult approvalRejection(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        String auditOpinion = MapUtil.getStr(map, "auditOpinion");
        Integer status = MapUtil.getInt(map, "status");
        if (id == null || StringUtils.isEmpty(auditOpinion) || status == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }

        BusinessDevEvaluation tmp = new BusinessDevEvaluation()
                .setId(id).setStatus(3);

        AuditInfo auditInfo = new AuditInfo().setExtId(id)
                .setExtDesc(AuditTypeDescEnum.THREE.getTable())
                .setAuditComment(auditOpinion);

        switch (status) {
            case 0:
                auditInfo.setAuditStatus("1").setAuditTypeDesc("经理审核意见");
                tmp.setAuditOpinion(auditOpinion);
                break;
            case 1:
                auditInfo.setAuditStatus("2").setAuditTypeDesc("总经理审核意见");
                tmp.setEvaluationOpinion(auditOpinion);
        }

        this.businessDevEvaluationService.approvalRejection(auditInfo);
        return CommonResult.success();
    }
}

