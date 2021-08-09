package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.SystemRoleMapper;
import com.jayud.scm.model.bo.QueryRoleForm;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.vo.SystemRoleVO;
import com.jayud.scm.model.vo.SystemRoleView;
import com.jayud.scm.service.ISystemMenuService;
import com.jayud.scm.service.ISystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-21
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements ISystemRoleService {

    @Autowired
    ISystemMenuService menuService;

    @Override
    public List<SystemRoleVO> getRoleList(Long userId) {
        List<SystemRole> roleList = baseMapper.getRoleList(userId);
        return convertList(roleList);
    }

    @Override
    public Long saveRole(SystemRole role) {
        return baseMapper.saveRole(role);
    }

    @Override
    public List<SystemRoleVO> findRole() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        List<SystemRole> systemRoles = baseMapper.selectList(queryWrapper);
        return convertList(systemRoles);
    }

    @Override
    public IPage<SystemRoleView> findRoleByPage(QueryRoleForm form) {
        //定义分页参数
        Page<SystemRoleView> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SystemRoleView> pageInfo = baseMapper.findRoleByPage(page, form);
        return pageInfo;
    }

    @Override
    public SystemRole getRoleByCondition(Map<String, Object> param) {
        return baseMapper.getRoleByCondition(param);
    }


    /**
     * 参数转换
     * @param roleList
     * @return
     */
    private List<SystemRoleVO> convertList(List<SystemRole> roleList){
        return ConvertUtil.convertList(roleList,SystemRoleVO.class);
    }



}
