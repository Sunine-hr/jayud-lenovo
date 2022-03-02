package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmContractQuotationDetails;
import com.jayud.crm.mapper.CrmContractQuotationDetailsMapper;
import com.jayud.crm.service.ICrmContractQuotationDetailsService;
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
 * 合同报价详情 服务实现类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Service
public class CrmContractQuotationDetailsServiceImpl extends ServiceImpl<CrmContractQuotationDetailsMapper, CrmContractQuotationDetails> implements ICrmContractQuotationDetailsService {


    @Autowired
    private CrmContractQuotationDetailsMapper crmContractQuotationDetailsMapper;

    @Override
    public IPage<CrmContractQuotationDetails> selectPage(CrmContractQuotationDetails crmContractQuotationDetails,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmContractQuotationDetails> page=new Page<CrmContractQuotationDetails>(currentPage,pageSize);
        IPage<CrmContractQuotationDetails> pageList= crmContractQuotationDetailsMapper.pageList(page, crmContractQuotationDetails);
        return pageList;
    }

    @Override
    public List<CrmContractQuotationDetails> selectList(CrmContractQuotationDetails crmContractQuotationDetails){
        return crmContractQuotationDetailsMapper.list(crmContractQuotationDetails);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmContractQuotationDetailsMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmContractQuotationDetailsMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmContractQuotationDetailsForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmContractQuotationDetailsForExcel(paramMap);
    }

}
