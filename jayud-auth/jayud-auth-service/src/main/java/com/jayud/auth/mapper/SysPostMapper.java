package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysPost;
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
 * 岗位表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-22
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: page
     * @param: sysPost
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysPost>
     **/
    IPage<SysPost> pageList(@Param("page") Page<SysPost> page, @Param("sysPost") SysPost sysPost);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-22
     * @param: sysPost
     * @return: java.util.List<com.jayud.auth.model.po.SysPost>
     **/
    List<SysPost> list(@Param("sysPost") SysPost sysPost);


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
}
