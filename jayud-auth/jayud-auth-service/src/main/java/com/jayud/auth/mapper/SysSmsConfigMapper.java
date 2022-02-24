package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysSmsConfig;
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
 * 系统短信配置表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-24
 */
@Mapper
public interface SysSmsConfigMapper extends BaseMapper<SysSmsConfig> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-24
     * @param: page
     * @param: sysSmsConfig
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysSmsConfig>
     **/
    IPage<SysSmsConfig> pageList(@Param("page") Page<SysSmsConfig> page, @Param("sysSmsConfig") SysSmsConfig sysSmsConfig);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-24
     * @param: sysSmsConfig
     * @return: java.util.List<com.jayud.auth.model.po.SysSmsConfig>
     **/
    List<SysSmsConfig> list(@Param("sysSmsConfig") SysSmsConfig sysSmsConfig);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);
}
