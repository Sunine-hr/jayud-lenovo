package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.crm.model.bo.CrmCreditVisitForm;
import com.jayud.crm.model.vo.CrmCreditVisitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCreditVisit;
import com.jayud.crm.mapper.CrmCreditVisitMapper;
import com.jayud.crm.service.ICrmCreditVisitService;
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
 * 基本档案_客户_客户走访记录 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCreditVisitServiceImpl extends ServiceImpl<CrmCreditVisitMapper, CrmCreditVisit> implements ICrmCreditVisitService {


    @Autowired
    private CrmCreditVisitMapper crmCreditVisitMapper;

    @Override
    public IPage<CrmCreditVisitVO> selectPage(CrmCreditVisitForm crmCreditVisitForm,
                                            Integer currentPage,
                                            Integer pageSize,
                                            HttpServletRequest req){

        Page<CrmCreditVisitForm> page=new Page<CrmCreditVisitForm>(currentPage,pageSize);
        IPage<CrmCreditVisitVO> pageList= crmCreditVisitMapper.pageList(page, crmCreditVisitForm);
        return pageList;
    }

    @Override
    public List<CrmCreditVisit> selectList(CrmCreditVisit crmCreditVisit){
        return crmCreditVisitMapper.list(crmCreditVisit);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCreditVisitMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCreditVisitMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

}
