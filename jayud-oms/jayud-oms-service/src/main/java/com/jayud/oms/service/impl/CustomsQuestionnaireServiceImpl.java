package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.AddCustomsQuestionnaireForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.CustomsQuestionnaire;
import com.jayud.oms.mapper.CustomsQuestionnaireMapper;
import com.jayud.oms.model.vo.CustomsQuestionnaireVO;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.ICustomsQuestionnaireService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public void addOrUpdate(AddCustomsQuestionnaireForm form) {
        CustomsQuestionnaire tmp = ConvertUtil.convert(form, CustomsQuestionnaire.class);

        CustomerInfo customerInfo = customerInfoService.getByCode(tmp.getCustomerCode());
        tmp.setCustomerName(customerInfo.getName());

        if (tmp.getId() == null) {
            tmp.setRecorder(UserOperator.getToken());
            tmp.setEvaluationDate(LocalDateTime.now());
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
}
