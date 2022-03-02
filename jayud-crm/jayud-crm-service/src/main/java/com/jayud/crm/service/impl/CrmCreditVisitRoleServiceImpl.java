package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCreditVisitRole;
import com.jayud.crm.mapper.CrmCreditVisitRoleMapper;
import com.jayud.crm.service.ICrmCreditVisitRoleService;
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
 * 客户走访记录-拜访人员(员工表)关联表 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCreditVisitRoleServiceImpl extends ServiceImpl<CrmCreditVisitRoleMapper, CrmCreditVisitRole> implements ICrmCreditVisitRoleService {


    @Autowired
    private CrmCreditVisitRoleMapper crmCreditVisitRoleMapper;

    @Override
    public IPage<CrmCreditVisitRole> selectPage(CrmCreditVisitRole crmCreditVisitRole,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCreditVisitRole> page=new Page<CrmCreditVisitRole>(currentPage,pageSize);
        IPage<CrmCreditVisitRole> pageList= crmCreditVisitRoleMapper.pageList(page, crmCreditVisitRole);
        return pageList;
    }

    @Override
    public List<CrmCreditVisitRole> selectList(CrmCreditVisitRole crmCreditVisitRole){
        return crmCreditVisitRoleMapper.list(crmCreditVisitRole);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCreditVisitRoleMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCreditVisitRoleMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
