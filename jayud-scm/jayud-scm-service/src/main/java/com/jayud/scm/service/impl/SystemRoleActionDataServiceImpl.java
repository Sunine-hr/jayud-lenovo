package com.jayud.scm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.AddSystemRoleActionDataForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.enums.ActionEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.SystemRoleActionDataMapper;
import com.jayud.scm.model.vo.SystemActionOutVO;
import com.jayud.scm.model.vo.SystemRoleActionCheckVO;
import com.jayud.scm.model.vo.SystemRoleActionDataVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import javax.xml.ws.Action;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色数据权限 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Service
public class SystemRoleActionDataServiceImpl extends ServiceImpl<SystemRoleActionDataMapper, SystemRoleActionData> implements ISystemRoleActionDataService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemActionService systemActionService;

    @Autowired
    private ISystemMenuService systemMenuService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @Override
    public IPage<SystemRoleActionDataVO> findByPage(QueryForm form) {
        Page<SystemRoleActionDataVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean addSystemRoleActionData(AddSystemRoleActionDataForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

//        增加强先删除原来的审核按钮
        QueryWrapper<SystemRoleActionData> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleActionData::getRoleId,form.getRoleId());
        List<SystemRoleActionData> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            for (SystemRoleActionData systemRoleActionData : list) {
                systemRoleActionData.setVoided(1);
                systemRoleActionData.setVoidedBy(systemUser.getId().intValue());
                systemRoleActionData.setVoidedByDtm(LocalDateTime.now());
                systemRoleActionData.setVoidedByName(systemUser.getUserName());
            }
            boolean result = this.updateBatchById(list);
            if(result){
                log.warn("删除原来的审核权限成功");
            }
        }


        List<SystemRoleActionData> systemRoleActionDatas = new ArrayList<>();

        for (Integer integer : form.getActionId()) {

            SystemMenu systemMenu = systemMenuService.getById(integer);
            SystemRoleActionData systemRoleActionData = new SystemRoleActionData();

            systemRoleActionData.setCrtBy(systemUser.getId().intValue());
            systemRoleActionData.setCrtByDtm(LocalDateTime.now());
            systemRoleActionData.setCrtByName(systemUser.getUserName());
            systemRoleActionData.setRoleId(form.getRoleId());
            systemRoleActionData.setActionCode(systemMenu.getActionCode());
            systemRoleActionData.setActionId(systemMenu.getId().intValue());
            systemRoleActionData.setDateType(form.getDateType());
            systemRoleActionData.setRemark(form.getRemark());
            systemRoleActionDatas.add(systemRoleActionData);
        }


        boolean save = this.saveBatch(systemRoleActionDatas);
        if(!save){
            log.warn("角色数据权限增加失败："+systemRoleActionDatas);
        }
        return save;
    }

    /**
     * 获取数据权限
     */
    public List<Integer> getRoleData(String actionCode){
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
        List<Long> longs = new ArrayList<>();
        for (SystemRole systemRole : enabledRolesByUserId) {
            longs.add(systemRole.getId());
        }
        List<Integer> list = new ArrayList<>();
        Integer result = this.baseMapper.getRoleData(actionCode,longs);
        if(result != null){
            if(result.equals(ActionEnum.ZERO.getCode())){
                list.add(0);

            }
            if(result.equals(ActionEnum.ONE.getCode())){
                list.add(systemUser.getId().intValue());
                return list;
            }
            if(result.equals(ActionEnum.TWO.getCode())){
                List<SystemUser> systemUsers = systemUserService.getSystemUserByDepartmentId(systemUser.getDepartmentId());
                for (SystemUser user : systemUsers) {
                    list.add(user.getId().intValue());
                }
                return list;
            }
            if(result.equals(ActionEnum.THREE.getCode())){
                return null;
            }
        }
        return list;
    }
}
