package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysArea;
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
 * 系统-中国行政地区表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-26
 */
@Mapper
public interface SysAreaMapper extends BaseMapper<SysArea> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-26
     * @param: page
     * @param: sysArea
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysArea>
     **/
    IPage<SysArea> pageList(@Param("page") Page<SysArea> page, @Param("sysArea") SysArea sysArea);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-26
     * @param: sysArea
     * @return: java.util.List<com.jayud.auth.model.po.SysArea>
     **/
    List<SysArea> list(@Param("sysArea") SysArea sysArea);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-26
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-26
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);

    List<SysArea> selectAreas();
}
