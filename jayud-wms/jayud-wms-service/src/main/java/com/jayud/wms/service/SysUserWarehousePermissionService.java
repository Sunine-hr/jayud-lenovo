package com.jayud.wms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.SysUserWarehousePermission;
import com.jayud.wms.model.vo.SysDataPermissionVo;
import com.jayud.wms.model.vo.SysUserWarehousePermissionVo;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户与仓库权限关联表 服务类
 *
 * @author jyd
 * @since 2021-12-14
 */
public interface SysUserWarehousePermissionService extends IService<SysUserWarehousePermission> {

    /**
     * @description 根据条件查询用户、仓库关系
     * @author  ciro
     * @date   2021/12/14 9:54
     * @param: sysUserWarehousePermissionVo
     * @return: java.util.List<com.jyd.admin.vo.SysUserWarehousePermissionVo>
     **/
    List<SysUserWarehousePermissionVo> query(SysUserWarehousePermissionVo sysUserWarehousePermissionVo);

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2021/12/14 10:06
     * @param: sysDataPermissionVo
     * @return: void
     **/
    void save(SysDataPermissionVo sysDataPermissionVo);


    /**
     * @description 根据用户id、仓库id删除
     * @author  ciro
     * @date   2021/12/14 10:21
     * @param: userId
     * @param: warehouseIds
     * @return: void
     **/
    void del(String userId,List<String> warehouseIds);

    /**
     * @description 根据用户id查询
     * @author  ciro
     * @date   2021/12/14 10:22
     * @param: userId
     * @return: java.util.List<java.lang.String>
     **/
    List<String> getWarehouseIdByUserId(String userId);

    /**
     * 根据用户id，查询用户仓库
     * @param userId
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWarehouseByUser(Long userId);
}
