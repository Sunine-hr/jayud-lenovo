package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.QuerySysDeptForm;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.dto.AuthUserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.mapper.SysDepartMapper;
import com.jayud.auth.service.ISysDepartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 组织机构表 服务实现类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {


    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public IPage<SysDepart> selectPage(SysDepart sysDepart,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysDepart> page=new Page<SysDepart>(currentPage,pageSize);
        IPage<SysDepart> pageList= sysDepartMapper.pageList(page, sysDepart);
        return pageList;
    }

    @Override
    public List<SysDepart> selectList(SysDepart sysDepart){
        return sysDepartMapper.list(sysDepart);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysDepartMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysDepartMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public List<SysDepart> selectDeptTree(QuerySysDeptForm form) {
        //组织区分租户
        AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
        SysUser sysUser = sysUserService.getById(userDetail.getId());
        String tenantCode = sysUser.getTenantCode();
        form.setTenantCode(tenantCode);
        List<SysDepart> departs = sysDepartMapper.selectDeptTree(form);
        List<SysDepart> tree = buildTree(departs, "0");
        return tree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSysDepart(SysDepart depart) {
        Long id = depart.getId();
        SysDepart sysDepart = this.getById(id);
        if(ObjectUtil.isEmpty(sysDepart)){
            //新增
            QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysDepart::getIsDeleted, 0);
            queryWrapper.lambda().eq(SysDepart::getOrgCode, depart.getOrgCode());
            queryWrapper.lambda().groupBy(SysDepart::getOrgCode);
            SysDepart one = this.getOne(queryWrapper);
            if(ObjectUtil.isNotEmpty(one)){
                new IllegalAccessException("机构编码已存在");
            }
        }else{
            //修改
            QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().ne(SysDepart::getId, id);
            queryWrapper.lambda().eq(SysDepart::getIsDeleted, 0);
            queryWrapper.lambda().eq(SysDepart::getOrgCode, depart.getOrgCode());
            queryWrapper.lambda().groupBy(SysDepart::getOrgCode);
            SysDepart one = this.getOne(queryWrapper);
            if(ObjectUtil.isNotEmpty(one)){
                new IllegalAccessException("机构编码已存在");
            }
        }
        this.saveOrUpdate(depart);
    }

    /**
     * 构建树
     *
     * @param list
     * @param pid
     * @return
     */
    private List<SysDepart> buildTree(List<SysDepart> list, String pid) {
        List<SysDepart> treeList = new ArrayList<>();
        list.forEach(l -> {
            if (StrUtil.equals(pid, l.getParentId().toString())) {
                l.setChildren(buildTree(list, l.getId().toString()));
                treeList.add(l);
            }
        });
        return treeList;
    }

}
