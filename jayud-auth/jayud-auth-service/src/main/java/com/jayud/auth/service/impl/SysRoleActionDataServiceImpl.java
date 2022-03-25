package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.mapper.SysRoleActionDataMapper;
import com.jayud.auth.model.bo.AddSysRoleActionDataForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.enums.ActionEnum;
import com.jayud.auth.model.po.SysMenu;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysRoleActionData;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.model.vo.SysRoleActionDataVO;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.ISysMenuService;
import com.jayud.auth.service.ISysRoleActionDataService;
import com.jayud.auth.service.ISysUserRoleService;
import com.jayud.auth.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class SysRoleActionDataServiceImpl extends ServiceImpl<SysRoleActionDataMapper, SysRoleActionData> implements ISysRoleActionDataService {

    @Autowired
    private ISysUserService systemUserService;

    @Autowired
    private ISysMenuService systemMenuService;

    @Autowired
    private ISysUserRoleService systemUserRoleRelationService;

    @Override
    public IPage<SysRoleActionDataVO> findByPage(QueryForm form) {
        Page<SysRoleActionDataVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean addSystemRoleActionData(AddSysRoleActionDataForm form) {
        SysUserVO systemUser = systemUserService.getSystemUserByName(CurrentUserUtil.getUsername());

//        增加前先删除原来的审核按钮
//        QueryWrapper<SysRoleActionData> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SysRoleActionData::getRoleId,form.getRoleId());
//        List<SysRoleActionData> list = this.list(queryWrapper);
//        if(CollectionUtil.isNotEmpty(list)){
//            for (SysRoleActionData systemRoleActionData : list) {
//                systemRoleActionData.setVoided(1);
//                systemRoleActionData.setVoidedBy(systemUser.getId().intValue());
//                systemRoleActionData.setVoidedByDtm(LocalDateTime.now());
//                systemRoleActionData.setVoidedByName(systemUser.getUserName());
//            }
//            boolean result = this.updateBatchById(list);
//            if(result){
//                log.warn("删除原来的审核权限成功");
//            }
//        }


//        List<SysRoleActionData> systemRoleActionDatas = new ArrayList<>();
//
//        for (Integer integer : form.getActionId()) {
//
//            SysMenu systemMenu = systemMenuService.getById(integer);
//            SysRoleActionData systemRoleActionData = new SysRoleActionData();
//
//            systemRoleActionData.setCrtBy(systemUser.getId().intValue());
//            systemRoleActionData.setCrtByDtm(LocalDateTime.now());
//            systemRoleActionData.setCrtByName(systemUser.getUserName());
//            systemRoleActionData.setRoleId(form.getRoleId());
//            systemRoleActionData.setActionCode(systemMenu.getCode());
//            systemRoleActionData.setActionId(systemMenu.getId().intValue());
//            systemRoleActionData.setDateType(form.getDateType());
//            systemRoleActionData.setRemark(form.getRemark());
//            systemRoleActionDatas.add(systemRoleActionData);
//        }
        SysRoleActionData sysRoleActionData = ConvertUtil.convert(form, SysRoleActionData.class);
        if(null == sysRoleActionData.getId()){
            sysRoleActionData.setCrtBy(systemUser.getId().intValue());
            sysRoleActionData.setCrtByDtm(LocalDateTime.now());
            sysRoleActionData.setCrtByName(systemUser.getUserName());
        }else{
            sysRoleActionData.setMdyBy(systemUser.getId().intValue());
            sysRoleActionData.setMdyByDtm(LocalDateTime.now());
            sysRoleActionData.setMdyByName(systemUser.getUserName());
        }


        boolean save = this.saveOrUpdate(sysRoleActionData);
        if(!save){
            log.warn("角色数据权限增加失败：");
        }
        return save;
    }

    /**
     * 获取数据权限
     */
    @Override
    public List<Integer> getRoleData(String actionCode){
        SysUserVO systemUser = systemUserService.getSystemUserByName(CurrentUserUtil.getUsername());
        List<SysRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
        List<Long> longs = new ArrayList<>();
        for (SysRole systemRole : enabledRolesByUserId) {
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
                List<SysUser> systemUsers = systemUserService.getSysUserByDepartmentId(systemUser.getDepartId());
                for (SysUser user : systemUsers) {
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
