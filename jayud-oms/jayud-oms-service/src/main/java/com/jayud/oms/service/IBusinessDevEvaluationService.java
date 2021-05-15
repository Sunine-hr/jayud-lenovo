package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddBusinessDevEvaluationForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.BusinessDevEvaluation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.BusinessDevEvaluationVO;

/**
 * <p>
 * 商业伙伴开发评估表 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-04-26
 */
public interface IBusinessDevEvaluationService extends IService<BusinessDevEvaluation> {

    void approvalRejection(AuditInfo auditInfo);

    void addOrUpdate(AddBusinessDevEvaluationForm form);

    int countByCondition(BusinessDevEvaluation businessDevEvaluation);

    IPage<BusinessDevEvaluationVO> findByPage(QueryCustomsQuestionnaireForm form);
}
