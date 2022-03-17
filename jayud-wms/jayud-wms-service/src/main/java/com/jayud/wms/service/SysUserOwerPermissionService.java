package com.jayud.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.SysUserOwerPermission;
import com.jayud.wms.model.vo.SysDataPermissionVo;
import com.jayud.wms.model.vo.SysUserOwerPermissionVo;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * 业务逻辑接口层
 */
public interface SysUserOwerPermissionService extends IService<SysUserOwerPermission> {

    /**
     * @description 根据条件查询用户、货主关系
     * @author  ciro
     * @date   2021/12/6 17:52
     * @param: sysUserOwerPermissionVo
     * @return: java.util.List<com.jyd.admin.vo.SysUserOwerPermissionVo>
     **/
    List<SysUserOwerPermissionVo> query(SysUserOwerPermissionVo sysUserOwerPermissionVo);

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2021/12/6 18:00
     * @param: sysUserOwerPermissionVo
     * @return: void
     **/
    void save(SysDataPermissionVo sysDataPermissionVo);

    /**
     * @description     删除数据
     * @author  ciro
     * @date   2021/12/7 9:53
     * @param: userId   用户id
     * @param: owerIds  货主id集合
     * @return: void
     **/
    void del(String userId,List<String> owerIds);

    /**
     * @description 根据用户id查询客户id集合
     * @author  ciro
     * @date   2021/12/8 17:04
     * @param: userId
     * @return: java.util.List<java.lang.String>
     **/
    List<String> getOwerIdByUserId(String userId);


    /**
     * 根据用户id，查询用户货主
     * @param userId 用户id
     * @return
     */
    List<LinkedHashMap<String, Object>> queryOwerByUser(Long userId);
}
