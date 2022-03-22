package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysRoleActionCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.jayud.auth.model.vo.SysRoleActionCheckVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色审核级别权限 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-01
 */
@Mapper
public interface SysRoleActionCheckMapper extends BaseMapper<SysRoleActionCheck> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: page
     * @param: sysRoleActionCheck
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysRoleActionCheck>
     **/
    IPage<SysRoleActionCheckVO> pageList(@Param("page") Page<SysRoleActionCheckVO> page, @Param("sysRoleActionCheck") SysRoleActionCheck sysRoleActionCheck);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: sysRoleActionCheck
     * @return: java.util.List<com.jayud.auth.model.po.SysRoleActionCheck>
     **/
    List<SysRoleActionCheck> list(@Param("sysRoleActionCheck") SysRoleActionCheck sysRoleActionCheck);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-01
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySysRoleActionCheckForExcel(Map<String, Object> paramMap);

    /**
     * 获取按钮有几个审核级别
     * @param menuCode
     * @return
     */
    List<SysRoleActionCheckVO> getList(@Param("menuCode")String menuCode);

    /**
     * 获取该级审核有哪些角色
     * @param checkLevel
     * @param menuCode
     * @return
     */
    List<SysRoleActionCheckVO> getListByCheckLevelAndMenuCode(@Param("checkLevel")Integer checkLevel, @Param("menuCode")String menuCode);
}
