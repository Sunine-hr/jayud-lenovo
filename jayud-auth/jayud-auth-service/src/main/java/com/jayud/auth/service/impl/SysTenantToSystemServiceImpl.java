package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysTenantForm;
import com.jayud.common.utils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysTenantToSystem;
import com.jayud.auth.mapper.SysTenantToSystemMapper;
import com.jayud.auth.service.ISysTenantToSystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 租户-系统关联表 服务实现类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Service
public class SysTenantToSystemServiceImpl extends ServiceImpl<SysTenantToSystemMapper, SysTenantToSystem> implements ISysTenantToSystemService {


    @Autowired
    private SysTenantToSystemMapper sysTenantToSystemMapper;

    @Override
    public IPage<SysTenantToSystem> selectPage(SysTenantToSystem sysTenantToSystem,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysTenantToSystem> page=new Page<SysTenantToSystem>(currentPage,pageSize);
        IPage<SysTenantToSystem> pageList= sysTenantToSystemMapper.pageList(page, sysTenantToSystem);
        return pageList;
    }

    @Override
    public List<SysTenantToSystem> selectList(SysTenantToSystem sysTenantToSystem){
        return sysTenantToSystemMapper.list(sysTenantToSystem);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysTenantToSystemMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysTenantToSystemMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public void saveTenantSystemRelation(SysTenantForm sysTenantForm) {
        SysTenantToSystem sysTenantToSystem = new SysTenantToSystem();
        sysTenantToSystem.setTenantId(sysTenantForm.getId());
        List<SysTenantToSystem> tenantToSystemList = selectList(sysTenantToSystem);
        List<String> lastIdList = tenantToSystemList.stream().map(x->String.valueOf(x.getSystemId())).distinct().collect(Collectors.toList());
        List<String> thisIdList = sysTenantForm.getSysUrlList().stream().map(x->String.valueOf(x.getId())).distinct().collect(Collectors.toList());
        List<String> addIdList = ListUtil.getDiff(lastIdList,thisIdList);
        List<String> delIdList = ListUtil.getDiff(thisIdList,lastIdList);
        if (CollUtil.isNotEmpty(addIdList)){
            List<SysTenantToSystem> addList = new ArrayList<>();
            addIdList.forEach(id->{
                SysTenantToSystem system = new SysTenantToSystem();
                system.setSystemId(Long.valueOf(id));
                system.setTenantId(sysTenantForm.getId());
                addList.add(system);
            });
            this.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(delIdList)){
            sysTenantToSystemMapper.deletedRelation(sysTenantForm.getId(),delIdList,CurrentUserUtil.getUsername());
        }
    }

}
