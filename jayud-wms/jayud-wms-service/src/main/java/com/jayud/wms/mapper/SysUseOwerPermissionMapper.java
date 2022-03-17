package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.wms.model.po.SysUserOwerPermission;
import com.jayud.wms.model.vo.SysUserOwerPermissionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * 数据持久接口层
 */
@Mapper
public interface SysUseOwerPermissionMapper extends BaseMapper<SysUserOwerPermission> {

    /**
     * @description 根据条件查询用户、货主关系
     * @author  ciro
     * @date   2021/12/6 17:55
     * @param: sysUserOwerPermissionVo
     * @return: java.util.List<com.jyd.admin.vo.SysUserOwerPermissionVo>
     **/
    List<SysUserOwerPermissionVo> query(@Param("sysUserOwerPermissionVo") SysUserOwerPermissionVo sysUserOwerPermissionVo);

    /**
     * @description 批量新增
     * @author  ciro
     * @date   2021/12/7 9:44
     * @param: permissionList
     * @return: int
     **/
    int addBatch(@Param("permissionList") List<SysUserOwerPermission> permissionList);

    /**
     * @description   根据用户id、货主id集合逻辑删除
     * @author  ciro
     * @date   2021/12/7 9:45
     * @param: userId   用户id
     * @param: owerIds   货主id集合
     * @param: updateBy 更新人
     * @return: void
     **/
    void delByUserIdAndOwerId(@Param("userId") String userId,@Param("owerIds") String owerIds,@Param("updateBy") String updateBy);

    /**
     * 根据用户id，查询用户货主
     * @param userId
     * @return
     */
    List<LinkedHashMap<String, Object>> queryOwerByUser(@Param("userId") Long userId);
}
