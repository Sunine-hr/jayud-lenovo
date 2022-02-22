package com.jayud.auth.mapper;

import com.jayud.auth.model.bo.QuerySysDeptForm;
import com.jayud.auth.model.po.SysDepart;
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
 * 组织机构表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-22
 */
@Mapper
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: page
     * @param: sysDepart
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysDepart>
     **/
    IPage<SysDepart> pageList(@Param("page") Page<SysDepart> page, @Param("sysDepart") SysDepart sysDepart);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-22
     * @param: sysDepart
     * @return: java.util.List<com.jayud.auth.model.po.SysDepart>
     **/
    List<SysDepart> list(@Param("sysDepart") SysDepart sysDepart);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);

    /**
     * 查询部门树list
     * @param form
     * @return
     */
    List<SysDepart> selectDeptTree(@Param("form") QuerySysDeptForm form);
}
