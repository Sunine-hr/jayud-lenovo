package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.AddCustomsQuestionnaireForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.enums.CustomsQuestionnaireStatusEnum;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.jayud.oms.mapper.CustomsQuestionnaireMapper;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.ICustomsQuestionnaireService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 海关调查问卷 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-04-14
 */
@Service
public class CustomsQuestionnaireServiceImpl extends ServiceImpl<CustomsQuestionnaireMapper, CustomsQuestionnaire> implements ICustomsQuestionnaireService {

    @Autowired
    private ICustomerInfoService customerInfoService;
    @Autowired
    private IAuditInfoService auditInfoService;

    @Override
    public void addOrUpdate(AddCustomsQuestionnaireForm form) {
        CustomsQuestionnaire tmp = ConvertUtil.convert(form, CustomsQuestionnaire.class);
        CustomerInfo customerInfo = customerInfoService.getByCode(tmp.getCustomerCode());
        tmp.setCustomerName(customerInfo.getName());
        if (tmp.getId() == null) {
            LocalDateTime now = LocalDateTime.now();
            tmp.setRecorder(UserOperator.getToken())
                    .setOrderNo(generateOrderRules(form.getType())).setExpiresTime(now.plusMonths(12))
                    .setEvaluationDate(LocalDateTime.now());
        } else {
            tmp.setUpdateTime(LocalDateTime.now());
            tmp.setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(tmp);
    }


    @Override
    public IPage<CustomsQuestionnaireVO> findByPage(QueryCustomsQuestionnaireForm form) {
        Page<CustomsQuestionnaireVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    @Transactional
    public void approvalRejection(AuditInfo auditInfo) {
        CustomsQuestionnaire customsQuestionnaire = new CustomsQuestionnaire()
                .setId(auditInfo.getExtId()).setStatus(0).setAuditOpinion(auditInfo.getAuditComment());
        this.baseMapper.updateById(customsQuestionnaire);
        this.auditInfoService.save(auditInfo);
    }


    public String generateOrderRules(Integer type) {
        //(0:客户,1:供应商)
        String prefix = type == 0 ? "CS" : "VN";
        String count = StringUtils.supplyZero(this.count() + 1, 4);
        return prefix + DateUtils.format(new Date(), "yyyy") + count;
    }
}
