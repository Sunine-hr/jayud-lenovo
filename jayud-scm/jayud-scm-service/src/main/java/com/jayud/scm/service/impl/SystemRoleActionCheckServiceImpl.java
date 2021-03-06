package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddSystemRoleActionCheckForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.po.SystemRoleActionCheck;
import com.jayud.scm.mapper.SystemRoleActionCheckMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.SystemActionOutVO;
import com.jayud.scm.model.vo.SystemRoleActionCheckNodeVO;
import com.jayud.scm.model.vo.SystemRoleActionCheckVO;
import com.jayud.scm.model.vo.SystemUserSimpleVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色审核级别权限 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Service
public class SystemRoleActionCheckServiceImpl extends ServiceImpl<SystemRoleActionCheckMapper, SystemRoleActionCheck> implements ISystemRoleActionCheckService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemActionService systemActionService;

    @Autowired
    private ISystemRoleService systemRoleService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @Override
    public IPage<SystemRoleActionCheckVO> findByPage(QueryForm form) {
        Page<SystemRoleActionCheckVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<SystemRoleActionCheck> systemRoleActionChecks = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            SystemRoleActionCheck systemRoleActionCheck = new SystemRoleActionCheck();
            systemRoleActionCheck.setId(id.intValue());
            systemRoleActionCheck.setVoided(1);
            systemRoleActionCheck.setVoidedBy(deleteForm.getId().intValue());
            systemRoleActionCheck.setVoidedByDtm(deleteForm.getDeleteTime());
            systemRoleActionCheck.setVoidedByName(deleteForm.getName());
            systemRoleActionChecks.add(systemRoleActionCheck);
        }
        boolean b = this.updateBatchById(systemRoleActionChecks);
        if(b){
            log.warn("角色审核权限删除成功："+systemRoleActionChecks);
        }
        return b;
    }

    @Override
    public boolean addSystemRoleAction(AddSystemRoleActionCheckForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //增加强先删除原来的审核按钮
        QueryWrapper<SystemRoleActionCheck> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleActionCheck::getRoleId,form.getRoleId());
        boolean remove = this.remove(queryWrapper);
        if(remove){
            log.warn("删除原来的审核权限");
        }

        List<SystemRoleActionCheck> systemRoleActionChecks = new ArrayList<>();

        for (Integer integer : form.getActionId()) {

            SystemActionOutVO systemActionById = systemActionService.getSystemActionById(integer);
            SystemRoleActionCheck systemRoleActionCheck = new SystemRoleActionCheck();

            systemRoleActionCheck.setCrtBy(systemUser.getId().intValue());
            systemRoleActionCheck.setCrtByDtm(LocalDateTime.now());
            systemRoleActionCheck.setCrtByName(systemUser.getUserName());
            systemRoleActionCheck.setRoleId(form.getRoleId());
            systemRoleActionCheck.setActionCode(systemActionById.getActionCode());
            systemRoleActionCheck.setActionId(systemActionById.getId());
            systemRoleActionCheck.setCheckLevel(form.getCheckLevel() != null ?form.getCheckLevel():null);
            systemRoleActionCheck.setCheckMoney(form.getCheckMoney() != null ?form.getCheckMoney():null);
            systemRoleActionChecks.add(systemRoleActionCheck);
        }


        boolean save = this.saveBatch(systemRoleActionChecks);
        if(!save){
            log.warn("角色审核权限增加失败："+systemRoleActionChecks);
        }
        return save;
    }

    @Override
    public List<SystemRoleActionCheckNodeVO> getAuditNode(QueryCommonForm form) {
        QueryWrapper<SystemRoleActionCheck> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemRoleActionCheck::getActionCode,form.getActionCode());
        queryWrapper.lambda().eq(SystemRoleActionCheck::getVoided,0);
        List<SystemRoleActionCheck> list = this.list(queryWrapper);
        List<SystemRoleActionCheckNodeVO> systemRoleActionCheckNodeVOS = new ArrayList<>();
        for (SystemRoleActionCheck systemRoleActionCheck : list) {
            SystemRoleActionCheckNodeVO systemRoleActionCheckNodeVO = new SystemRoleActionCheckNodeVO();
            systemRoleActionCheckNodeVO.setCheckLevel(systemRoleActionCheck.getCheckLevel());
            systemRoleActionCheckNodeVO.setId(systemRoleActionCheck.getId());
            List<SystemUserSimpleVO> systemUserSimpleByRoleId = systemUserRoleRelationService.getSystemUserSimpleByRoleId(systemRoleActionCheck.getRoleId().longValue());
            if(CollectionUtils.isNotEmpty(systemUserSimpleByRoleId)){
                StringBuffer stringBuffer = new StringBuffer();
                for (SystemUserSimpleVO systemUserSimpleVO : systemUserSimpleByRoleId) {
                    stringBuffer.append(systemUserSimpleVO.getUserName()).append(",");
                }
                systemRoleActionCheckNodeVO.setReviewer(stringBuffer.toString().substring(0,stringBuffer.length()-1));

            }
            SystemRole byId = systemRoleService.getById(systemRoleActionCheck.getRoleId());
            systemRoleActionCheckNodeVO.setName(byId.getName());
            systemRoleActionCheckNodeVOS.add(systemRoleActionCheckNodeVO);
        }
        return systemRoleActionCheckNodeVOS;
    }
}
