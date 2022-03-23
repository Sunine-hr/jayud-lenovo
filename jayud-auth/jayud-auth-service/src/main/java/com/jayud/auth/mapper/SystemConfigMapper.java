package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SystemConfig;
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
 * 系统配置表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-23
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: page
     * @param: systemConfig
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SystemConfig>
     **/
    IPage<SystemConfig> pageList(@Param("page") Page<SystemConfig> page, @Param("systemConfig") SystemConfig systemConfig);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: systemConfig
     * @return: java.util.List<com.jayud.auth.model.po.SystemConfig>
     **/
    List<SystemConfig> list(@Param("systemConfig") SystemConfig systemConfig);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-23
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySystemConfigForExcel(Map<String, Object> paramMap);
}
