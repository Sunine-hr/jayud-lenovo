package com.jayud.scm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.scm.mapper.SystemRoleActionMapper;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemAction;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.po.SystemRoleAction;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.SystemRoleActionVO;
import com.jayud.scm.service.ISystemActionService;
import com.jayud.scm.service.ISystemRoleActionService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色权限设置（按钮） 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Service
public class SystemRoleActionServiceImpl extends ServiceImpl<SystemRoleActionMapper, SystemRoleAction> implements ISystemRoleActionService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemActionService systemActionService;


    @Override
    public SystemRoleAction getSystemRoleActionByRoleIdAndActionCode(Long id, String actionCode) {
        QueryWrapper<SystemRoleAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemRoleAction::getRoleId,id);
        queryWrapper.lambda().eq(SystemRoleAction::getActionCode,actionCode);
        queryWrapper.lambda().eq(SystemRoleAction::getVoided,0);

        List<SystemRoleAction> systemRoleActionList = this.baseMapper.selectList(queryWrapper);
        SystemRoleAction systemRoleAction = null;
        if(CollUtil.isNotEmpty(systemRoleActionList)){
            systemRoleAction = systemRoleActionList.get(0);
        }
        return systemRoleAction;
    }

    @Override
    public IPage<SystemRoleActionVO> findByPage(QueryForm form) {

        Page<SystemRoleActionVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public List<SystemRoleAction> findSystemRoleActionByRoleId(int parseInt) {
        QueryWrapper<SystemRoleAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemRoleAction::getRoleId,parseInt);
        queryWrapper.lambda().eq(SystemRoleAction::getVoided,0);
        return this.list(queryWrapper);
    }

    @Override
    public boolean removeSystemRoleActionByRoleId(List<Long> roleIds) {
        return this.baseMapper.removeSystemRoleActionByRoleId(roleIds);
    }

    @Override
    public void createSystemRoleAction(SystemRole systemRole, List<String> menuIds) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        menuIds.forEach(m -> {

            SystemAction systemAction = systemActionService.getSystemActionByActionCode(m);
            if(systemAction != null){
                SystemRoleAction systemRoleAction = new SystemRoleAction();
                systemRoleAction.setRoleId(systemRole.getId().intValue());
                systemRoleAction.setActionId(systemAction.getId());
                systemRoleAction.setActionCode(m);
                systemRoleAction.setCrtBy(systemUser.getId().intValue());
                systemRoleAction.setCrtByDtm(LocalDateTime.now());
                systemRoleAction.setCrtByName(systemUser.getUserName());
                systemRoleAction.insert();
            }
        });
        log.warn("增加角色按钮信息成功"+systemRole);
    }

    @Override
    public List<SystemRoleAction> getSystemRoleActionByRoleIdsAndActionCode(List<Long> longs, String actionCode) {
        QueryWrapper<SystemRoleAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SystemRoleAction::getRoleId,longs);
        queryWrapper.lambda().eq(SystemRoleAction::getActionCode,actionCode);
        queryWrapper.lambda().eq(SystemRoleAction::getVoided,0);

        List<SystemRoleAction> systemRoleActionList = this.baseMapper.selectList(queryWrapper);
        return systemRoleActionList;
    }
}
