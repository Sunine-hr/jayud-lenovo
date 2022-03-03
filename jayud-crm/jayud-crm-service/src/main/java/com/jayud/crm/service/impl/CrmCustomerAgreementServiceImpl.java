package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerAgreement;
import com.jayud.crm.mapper.CrmCustomerAgreementMapper;
import com.jayud.crm.service.ICrmCustomerAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_协议管理(crm_customer_agreement) 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCustomerAgreementServiceImpl extends ServiceImpl<CrmCustomerAgreementMapper, CrmCustomerAgreement> implements ICrmCustomerAgreementService {


    @Autowired
    private CrmCustomerAgreementMapper crmCustomerAgreementMapper;

    @Override
    public IPage<CrmCustomerAgreement> selectPage(CrmCustomerAgreement crmCustomerAgreement,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCustomerAgreement> page=new Page<CrmCustomerAgreement>(currentPage,pageSize);
        IPage<CrmCustomerAgreement> pageList= crmCustomerAgreementMapper.pageList(page, crmCustomerAgreement);
        return pageList;
    }

    @Override
    public List<CrmCustomerAgreement> selectList(CrmCustomerAgreement crmCustomerAgreement){
        return crmCustomerAgreementMapper.list(crmCustomerAgreement);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerAgreementMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerAgreementMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerAgreementForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerAgreementForExcel(paramMap);
    }

}
