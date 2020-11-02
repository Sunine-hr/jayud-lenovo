package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SystemRoleMapper;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.SaveRoleForm;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.vo.SystemRoleVO;
import com.jayud.mall.service.ISystemRoleMenuRelationService;
import com.jayud.mall.service.ISystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements ISystemRoleService {

    @Autowired
    SystemRoleMapper roleMapper;

    @Autowired
    ISystemRoleMenuRelationService roleMenuRelationService;


    @Override
    public void saveRole(SaveRoleForm saveRoleForm) {
        SystemRole systemRole = ConvertUtil.convert(saveRoleForm, SystemRole.class);
        if(systemRole.getId() != null){
            //修改
            this.saveOrUpdate(systemRole);
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(saveRoleForm.getId());
            //根据角色Id，清除角色菜单关联信息
            roleMenuRelationService.removeRoleMenuRelation(roleIds);
        }else{
            //新增
            roleMapper.saveRole(systemRole);
        }
        systemRole.setId(systemRole.getId());
        if(saveRoleForm.getMenuIds() != null){
            //关联角色菜单关联信息
            roleMenuRelationService.createRoleMenuRelation(systemRole, saveRoleForm.getMenuIds());
        }
    }

    @Override
    public void deleteRole(Long id) {
        roleMapper.deleteRole(id);
    }

    @Override
    public SystemRoleVO getRole(Long id) {
        SystemRoleVO roleVO = roleMapper.getRole(id);
        return roleVO;
    }

    @Override
    public IPage<SystemRoleVO> findRoleByPage(QueryRoleForm form) {
        //定义分页参数
        Page<SystemRoleVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SystemRoleVO> pageInfo = baseMapper.findRoleByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<SystemRole> selectRolesByUserId(Long userId) {
        List<SystemRole> list = roleMapper.selectRolesByUserId(userId);
        return list;
    }
}
