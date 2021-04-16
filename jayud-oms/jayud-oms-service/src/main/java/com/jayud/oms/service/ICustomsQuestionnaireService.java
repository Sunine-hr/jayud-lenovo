package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddCustomsQuestionnaireForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;

/**
 * <p>
 * 海关调查问卷 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-04-14
 */
public interface ICustomsQuestionnaireService extends IService<CustomsQuestionnaire> {

    void addOrUpdate(AddCustomsQuestionnaireForm form);

    IPage<CustomsQuestionnaireVO> findByPage(QueryCustomsQuestionnaireForm form);

    void approvalRejection(AuditInfo auditInfo);
}
