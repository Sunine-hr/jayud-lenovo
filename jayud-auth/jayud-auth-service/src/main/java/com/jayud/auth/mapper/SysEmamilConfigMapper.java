package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysEmamilConfig;
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
 * 系统邮箱配置表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-24
 */
@Mapper
public interface SysEmamilConfigMapper extends BaseMapper<SysEmamilConfig> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-24
     * @param: page
     * @param: sysEmamilConfig
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysEmamilConfig>
     **/
    IPage<SysEmamilConfig> pageList(@Param("page") Page<SysEmamilConfig> page, @Param("sysEmamilConfig") SysEmamilConfig sysEmamilConfig);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-24
     * @param: sysEmamilConfig
     * @return: java.util.List<com.jayud.auth.model.po.SysEmamilConfig>
     **/
    List<SysEmamilConfig> list(@Param("sysEmamilConfig") SysEmamilConfig sysEmamilConfig);


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
