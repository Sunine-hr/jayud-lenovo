package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.mapper.SysUseOwerPermissionMapper;
import com.jayud.wms.model.po.SysUserOwerPermission;
import com.jayud.wms.model.vo.SysDataPermissionVo;
import com.jayud.wms.model.vo.SysUserOwerPermissionVo;
import com.jayud.wms.service.SysUserOwerPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务逻辑实现层
 * @author Administrator
 */
@Service
@Transactional
public class SysUserOwerPermissionServiceImpl extends ServiceImpl<SysUseOwerPermissionMapper,SysUserOwerPermission> implements SysUserOwerPermissionService {

    @Autowired
    private SysUseOwerPermissionMapper sysUseOwerPermissionMapper;



    @Override
    public List<SysUserOwerPermissionVo> query(SysUserOwerPermissionVo sysUserOwerPermissionVo) {
        List<SysUserOwerPermissionVo> permissionVoList = sysUseOwerPermissionMapper.query(sysUserOwerPermissionVo);
        return permissionVoList;
    }

    @Override
    public void save(SysDataPermissionVo sysDataPermissionVo) {
        //新增货主id集合
        List<String> addOwerIds = ListUtils.getDiff(sysDataPermissionVo.getLastSelectOwerIds(), sysDataPermissionVo.getThisSelectOwerIds());
        //删除货主id集合
        List<String> delOwerIds = ListUtils.getDiff(sysDataPermissionVo.getThisSelectOwerIds(),sysDataPermissionVo.getLastSelectOwerIds());
        List<SysUserOwerPermission> permissionList = new ArrayList<>();
        if (addOwerIds!=null) {
            addOwerIds.forEach(owerId -> {
                SysUserOwerPermission sysUserOwerPermission = new SysUserOwerPermission();
                sysUserOwerPermission.setTenantCode(CurrentUserUtil.getUserTenantCode());
                sysUserOwerPermission.setUserId(sysDataPermissionVo.getUserId());
                sysUserOwerPermission.setOwerId(owerId);
                sysUserOwerPermission.setCreateBy(CurrentUserUtil.getUsername());
                permissionList.add(sysUserOwerPermission);
            });
            if (!permissionList.isEmpty()) {
                this.saveBatch(permissionList);
            }
        }
        if (delOwerIds != null) {
            if (!delOwerIds.isEmpty()) {
                sysUseOwerPermissionMapper.delByUserIdAndOwerId(sysDataPermissionVo.getUserId(), StringUtils.join(delOwerIds, ","), CurrentUserUtil.getUsername());
            }
        }

    }

    @Override
    public void del(String userId, List<String> owerIds) {
        sysUseOwerPermissionMapper.delByUserIdAndOwerId(userId,StringUtils.join(owerIds,","),CurrentUserUtil.getUsername());
    }

    @Override
    public List<String> getOwerIdByUserId(String userId) {
        SysUserOwerPermissionVo sysUserOwerPermissionVo = new SysUserOwerPermissionVo();
        sysUserOwerPermissionVo.setUserId(userId);
        sysUserOwerPermissionVo.setTenantCode(CurrentUserUtil.getUserTenantCode());
        List<SysUserOwerPermissionVo> permissionVoList = sysUseOwerPermissionMapper.query(sysUserOwerPermissionVo);
        List<String> owerIdList = permissionVoList.stream().filter(x -> x.getIsSelected()).map(x -> x.getOwerId()).distinct().collect(Collectors.toList());
        if (owerIdList.isEmpty()){
            owerIdList.add("null");
        }
        return owerIdList;
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryOwerByUser(Long userId) {
        return sysUseOwerPermissionMapper.queryOwerByUser(userId);
    }


}
