package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddSystemActionForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.HsCode;
import com.jayud.scm.model.po.SystemAction;
import com.jayud.scm.mapper.SystemActionMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.SystemActionOutVO;
import com.jayud.scm.model.vo.SystemActionVO;
import com.jayud.scm.model.vo.SystemRoleActionVO;
import com.jayud.scm.service.ISystemActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemMenuService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 按钮权限设置表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Service
public class SystemActionServiceImpl extends ServiceImpl<SystemActionMapper, SystemAction> implements ISystemActionService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemMenuService systemMenuService;

    @Override
    public IPage<SystemActionVO> findByPage(QueryForm form) {
        Page<SystemActionVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public SystemAction getSystemActionByActionCode(String actionCode) {
        QueryWrapper<SystemAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemAction::getActionCode,actionCode);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateSystemAction(AddSystemActionForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        SystemAction systemAction = ConvertUtil.convert(form, SystemAction.class);
        if(form.getId() != null){
            systemAction.setMdyBy(systemUser.getId().intValue());
            systemAction.setMdyByDtm(LocalDateTime.now());
            systemAction.setMdyByName(systemUser.getUserName());
        }else {
            systemAction.setCrtBy(systemUser.getId().intValue());
            systemAction.setCrtByDtm(LocalDateTime.now());
            systemAction.setCrtByName(systemUser.getUserName());
        }
        boolean update = this.saveOrUpdate(systemAction);
        if(!update){
            log.warn("按钮权限添加失败"+systemAction);
        }
        return update;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<SystemAction> systemActions = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            SystemAction systemAction = new SystemAction();
            systemAction.setId(id.intValue());
            systemAction.setVoided(1);
            systemAction.setVoidedBy(deleteForm.getId().intValue());
            systemAction.setVoidedByDtm(deleteForm.getDeleteTime());
            systemAction.setVoidedByName(deleteForm.getName());
            systemActions.add(systemAction);
        }
        boolean b = this.updateBatchById(systemActions);
        if(b){
            log.warn("按钮权限删除成功："+systemActions);
        }
        return b;
    }

    @Override
    public List<SystemAction> getSystemActionByIsAudit(Long id) {
        QueryWrapper<SystemAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemAction::getIsAudit,1);
        queryWrapper.lambda().eq(SystemAction::getVoided,0);
        queryWrapper.lambda().eq(SystemAction::getParentId,id);
        return this.list(queryWrapper);
    }

    @Override
    public List<SystemAction> getSystemActionList(Long id) {
        QueryWrapper<SystemAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemAction::getVoided,0);
        queryWrapper.lambda().eq(SystemAction::getIsAudit,1);
        queryWrapper.lambda().eq(SystemAction::getParentId,id);
        return this.list(queryWrapper);
    }

    @Override
    public SystemActionOutVO getSystemActionById(Integer id) {
        SystemAction byId = this.getById(id);
        SystemActionOutVO systemActionOutVO = ConvertUtil.convert(byId, SystemActionOutVO.class);
        systemActionOutVO.setTitle(systemMenuService.getById(systemActionOutVO.getParentId().longValue()).getTitle());
        return systemActionOutVO;
    }
}
