package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.mapper.SysUserWarehousePermissionMapper;
import com.jayud.wms.model.po.SysUserWarehousePermission;
import com.jayud.wms.model.vo.SysDataPermissionVo;
import com.jayud.wms.model.vo.SysUserWarehousePermissionVo;
import com.jayud.wms.service.SysUserWarehousePermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户与仓库权限关联表 服务实现类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Service
public class SysUserWarehousePermissionServiceImpl extends ServiceImpl<SysUserWarehousePermissionMapper, SysUserWarehousePermission> implements SysUserWarehousePermissionService {



    @Autowired
    private SysUserWarehousePermissionMapper sysUserWarehousePermissionMapper;


    @Override
    public List<SysUserWarehousePermissionVo> query(SysUserWarehousePermissionVo sysUserWarehousePermissionVo) {
        return sysUserWarehousePermissionMapper.query(sysUserWarehousePermissionVo);
    }

    @Override
    public void save(SysDataPermissionVo sysDataPermissionVo) {
        //新增货主id集合
        List<String> addWarehouseIds = ListUtils.getDiff(sysDataPermissionVo.getLastSelectWarehouseIds(), sysDataPermissionVo.getThisSelectWarehouseIds());
        //删除货主id集合
        List<String> delWarehouseIds = ListUtils.getDiff(sysDataPermissionVo.getThisSelectWarehouseIds(),sysDataPermissionVo.getLastSelectWarehouseIds());
        List<SysUserWarehousePermission> permissionList = new ArrayList<>();
        if (addWarehouseIds != null) {
            addWarehouseIds.forEach(warehouseId -> {
                SysUserWarehousePermission sysUserWarehousePermission = new SysUserWarehousePermission();
                sysUserWarehousePermission.setTenantCode(CurrentUserUtil.getUserTenantCode());
                sysUserWarehousePermission.setUserId(sysDataPermissionVo.getUserId());
                sysUserWarehousePermission.setWarehouseId(warehouseId);
                sysUserWarehousePermission.setCreateBy(CurrentUserUtil.getUsername());
                permissionList.add(sysUserWarehousePermission);
            });
            if (!permissionList.isEmpty()) {
                this.saveBatch(permissionList);
            }
        }
        if (delWarehouseIds != null) {
            if (!delWarehouseIds.isEmpty()) {
                sysUserWarehousePermissionMapper.delByUserIdAndWarehouseId(sysDataPermissionVo.getUserId(), StringUtils.join(delWarehouseIds, ","), CurrentUserUtil.getUsername());
            }
        }
    }

    @Override
    public void del(String userId, List<String> warehouseIds) {
        sysUserWarehousePermissionMapper.delByUserIdAndWarehouseId(userId,StringUtils.join(warehouseIds,","),CurrentUserUtil.getUsername());
    }

    @Override
    public List<String> getWarehouseIdByUserId(String userId) {
        SysUserWarehousePermissionVo sysUserWarehousePermissionVo = new SysUserWarehousePermissionVo();
        sysUserWarehousePermissionVo.setUserId(userId);
        sysUserWarehousePermissionVo.setTenantCode(CurrentUserUtil.getUserTenantCode());
        List<SysUserWarehousePermissionVo> permissionVoList = sysUserWarehousePermissionMapper.query(sysUserWarehousePermissionVo);
        List<String> warehourseIdList = permissionVoList.stream().filter(x -> x.getIsSelected()).map(x -> x.getWarehouseId()).distinct().collect(Collectors.toList());
        if (warehourseIdList.isEmpty()){
            warehourseIdList.add("null");
        }
        return warehourseIdList;
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWarehouseByUser(Long userId) {
        return sysUserWarehousePermissionMapper.queryWarehouseByUser(userId);
    }
}
