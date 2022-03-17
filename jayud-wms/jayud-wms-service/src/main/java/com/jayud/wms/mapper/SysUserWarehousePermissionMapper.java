package com.jayud.wms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.wms.model.po.SysUserWarehousePermission;
import com.jayud.wms.model.vo.SysUserWarehousePermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户与仓库权限关联表 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-14
 */
@Mapper
public interface SysUserWarehousePermissionMapper extends BaseMapper<SysUserWarehousePermission> {

    /**
     * @description 根据条件查询
     * @author  ciro
     * @date   2021/12/14 10:04
     * @param: sysUserWarehousePermissionVo
     * @return: java.util.List<com.jyd.admin.vo.SysUserWarehousePermissionVo>
     **/
    List<SysUserWarehousePermissionVo> query(@Param("sysUserWarehousePermissionVo") SysUserWarehousePermissionVo sysUserWarehousePermissionVo);

    /**
     * @description 批量新增
     * @author  ciro
     * @date   2021/12/14 10:13
     * @param: permissionList
     * @return: int
     **/
    int addBatch(@Param("permissionList") List<SysUserWarehousePermission> permissionList);

    /**
     * @description 根据用户、仓库id批量删除
     * @author  ciro
     * @date   2021/12/14 10:15
     * @param: userId   用户id
     * @param: warehouseIds 仓库id
     * @param: updateBy 更新人
     * @return: void
     **/
    void delByUserIdAndWarehouseId(@Param("userId") String userId,@Param("warehouseIds") String warehouseIds,@Param("updateBy") String updateBy);

    /**
     * 根据用户id，查询用户仓库
     * @param userId
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWarehouseByUser(@Param("userId") Long userId);
}
