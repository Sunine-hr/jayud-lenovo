package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysUserRole;
import com.jayud.auth.model.po.SysUserToWarehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户与仓库关联表 Mapper 接口
 *
 * @author jayud
 * @since 2022-04-08
 */
@Mapper
public interface SysUserToWarehouseMapper extends BaseMapper<SysUserToWarehouse> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-08
     * @param: page
     * @param: sysUserToWarehouse
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUserToWarehouse>
     **/
    IPage<SysUserToWarehouse> pageList(@Param("page") Page<SysUserToWarehouse> page, @Param("sysUserToWarehouse") SysUserToWarehouse sysUserToWarehouse);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-08
     * @param: sysUserToWarehouse
     * @return: java.util.List<com.jayud.auth.model.po.SysUserToWarehouse>
     **/
    List<SysUserToWarehouse> list(@Param("sysUserToWarehouse") SysUserToWarehouse sysUserToWarehouse);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-04-08
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-04-08
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    int updateSysUserToWarehouseTwo( @Param("SysUserToWarehouse") SysUserToWarehouse sysUserToWarehouse);
    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-08
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySysUserToWarehouseForExcel(Map<String, Object> paramMap);
}
