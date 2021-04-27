package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddBusinessDevEvaluationForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.BusinessDevEvaluation;
import com.jayud.oms.mapper.BusinessDevEvaluationMapper;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.jayud.oms.model.vo.BusinessDevEvaluationVO;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.IBusinessDevEvaluationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 商业伙伴开发评估表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-04-26
 */
@Service
public class BusinessDevEvaluationServiceImpl extends ServiceImpl<BusinessDevEvaluationMapper, BusinessDevEvaluation> implements IBusinessDevEvaluationService {

    @Autowired
    private IAuditInfoService auditInfoService;

    @Override
    public void approvalRejection(AuditInfo auditInfo) {
        BusinessDevEvaluation tmp = new BusinessDevEvaluation()
                .setId(auditInfo.getExtId()).setStatus(3).setAuditOpinion(auditInfo.getAuditComment());
        this.baseMapper.updateById(tmp);
        this.auditInfoService.save(auditInfo);
    }

    @Override
    public void addOrUpdate(AddBusinessDevEvaluationForm form) {
//        LocalDateTime dateTime = DateUtils.str2LocalDateTime(form.getSetYear(), "yyyy-MM");
        BusinessDevEvaluation tmp = ConvertUtil.convert(form, BusinessDevEvaluation.class);
        tmp.setAuditOpinion(" ");
        if (tmp.getId() == null) {
            LocalDateTime now = LocalDateTime.now();
            tmp.setCreateUser(UserOperator.getToken())
                    .setOrderNo(generateOrderRules(form.getType())).setExpiresTime(now.plusMonths(12))
                    .setCreateTime(LocalDateTime.now());
        } else {
            tmp.setUpdateTime(LocalDateTime.now());
            tmp.setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(tmp);
    }

    @Override
    public int countByCondition(BusinessDevEvaluation businessDevEvaluation) {
        QueryWrapper<BusinessDevEvaluation> condition = new QueryWrapper<>(businessDevEvaluation);
        return count(condition);
    }

    @Override
    public IPage<BusinessDevEvaluationVO> findByPage(QueryCustomsQuestionnaireForm form) {
        Page<CustomsQuestionnaireVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    public String generateOrderRules(Integer type) {
        //(0:客户,1:供应商)
        String prefix = type == 0 ? "CS" : "VN";
        String count = StringUtils.supplyZero(this.count() + 1, 4);
        return prefix + DateUtils.format(new Date(), "yyyy") + count;
    }
}
