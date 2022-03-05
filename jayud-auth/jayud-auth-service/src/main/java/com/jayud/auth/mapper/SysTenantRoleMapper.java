package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysTenantRole;
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
 * 租户规则配置 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-05
 */
@Mapper
public interface SysTenantRoleMapper extends BaseMapper<SysTenantRole> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-05
     * @param: page
     * @param: sysTenantRole
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysTenantRole>
     **/
    IPage<SysTenantRole> pageList(@Param("page") Page<SysTenantRole> page, @Param("sysTenantRole") SysTenantRole sysTenantRole);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-05
     * @param: sysTenantRole
     * @return: java.util.List<com.jayud.auth.model.po.SysTenantRole>
     **/
    List<SysTenantRole> list(@Param("sysTenantRole") SysTenantRole sysTenantRole);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-05
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-05
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-05
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySysTenantRoleForExcel(Map<String, Object> paramMap);
}
