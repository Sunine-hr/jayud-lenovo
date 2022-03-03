package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerTax;
import com.jayud.crm.mapper.CrmCustomerTaxMapper;
import com.jayud.crm.service.ICrmCustomerTaxService;
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
 * 开票资料 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCustomerTaxServiceImpl extends ServiceImpl<CrmCustomerTaxMapper, CrmCustomerTax> implements ICrmCustomerTaxService {


    @Autowired
    private CrmCustomerTaxMapper crmCustomerTaxMapper;

    @Override
    public IPage<CrmCustomerTax> selectPage(CrmCustomerTax crmCustomerTax,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCustomerTax> page=new Page<CrmCustomerTax>(currentPage,pageSize);
        IPage<CrmCustomerTax> pageList= crmCustomerTaxMapper.pageList(page, crmCustomerTax);
        return pageList;
    }

    @Override
    public List<CrmCustomerTax> selectList(CrmCustomerTax crmCustomerTax){
        return crmCustomerTaxMapper.list(crmCustomerTax);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerTaxMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerTaxMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerTaxForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerTaxForExcel(paramMap);
    }

}
