package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.QueryRoleForm;
import com.jayud.model.po.SystemRole;
import com.jayud.model.vo.SystemRoleVO;
import com.jayud.model.vo.SystemRoleView;
import com.jayud.oauth.mapper.SystemRoleMapper;
import com.jayud.oauth.service.ISystemMenuService;
import com.jayud.oauth.service.ISystemRoleService;
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
        List<SystemRole> systemRoles = baseMapper.selectList(null);
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
