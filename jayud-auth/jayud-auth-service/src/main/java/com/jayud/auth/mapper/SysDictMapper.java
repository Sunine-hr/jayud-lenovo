package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysDict;
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
 * 字典 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-23
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-23
     * @param: page
     * @param: sysDict
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysDict>
     **/
    IPage<SysDict> pageList(@Param("page") Page<SysDict> page, @Param("sysDict") SysDict sysDict);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-23
     * @param: sysDict
     * @return: java.util.List<com.jayud.auth.model.po.SysDict>
     **/
    List<SysDict> list(@Param("sysDict") SysDict sysDict);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-23
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-23
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);
}
