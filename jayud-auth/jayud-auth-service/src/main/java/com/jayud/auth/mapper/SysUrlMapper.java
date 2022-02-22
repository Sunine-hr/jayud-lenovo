package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysUrl;
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
 * 系统链接表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-21
 */
@Mapper
public interface SysUrlMapper extends BaseMapper<SysUrl> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: page
     * @param: sysUrl
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUrl>
     **/
    IPage<SysUrl> pageList(@Param("page") Page<SysUrl> page, @Param("sysUrl") SysUrl sysUrl);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUrl
     * @return: java.util.List<com.jayud.auth.model.po.SysUrl>
     **/
    List<SysUrl> list(@Param("sysUrl") SysUrl sysUrl);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);
}
