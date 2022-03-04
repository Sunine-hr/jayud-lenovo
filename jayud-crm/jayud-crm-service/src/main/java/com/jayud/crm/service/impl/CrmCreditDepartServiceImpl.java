package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.crm.model.bo.AddCrmCreditDepartForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCreditDepart;
import com.jayud.crm.mapper.CrmCreditDepartMapper;
import com.jayud.crm.service.ICrmCreditDepartService;
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
 * 基本档案_额度_部门额度授信管理(crm_credit_depart) 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCreditDepartServiceImpl extends ServiceImpl<CrmCreditDepartMapper, CrmCreditDepart> implements ICrmCreditDepartService {


    @Autowired
    private CrmCreditDepartMapper crmCreditDepartMapper;

    @Override
    public IPage<CrmCreditDepart> selectPage(CrmCreditDepart crmCreditDepart,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCreditDepart> page=new Page<CrmCreditDepart>(currentPage,pageSize);
        IPage<CrmCreditDepart> pageList= crmCreditDepartMapper.pageList(page, crmCreditDepart);
        return pageList;
    }

    @Override
    public List<CrmCreditDepart> selectList(CrmCreditDepart crmCreditDepart){
        return crmCreditDepartMapper.list(crmCreditDepart);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCreditDepartMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCreditDepartMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCreditDepartForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCreditDepartForExcel(paramMap);
    }

    @Override
    public void saveOrUpdate(AddCrmCreditDepartForm form) {

    }

}
