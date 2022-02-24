package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysEnterpriseDingdingConfig;
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
 * 系统钉钉配置表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-24
 */
@Mapper
public interface SysEnterpriseDingdingConfigMapper extends BaseMapper<SysEnterpriseDingdingConfig> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-24
     * @param: page
     * @param: sysEnterpriseDingdingConfig
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysEnterpriseDingdingConfig>
     **/
    IPage<SysEnterpriseDingdingConfig> pageList(@Param("page") Page<SysEnterpriseDingdingConfig> page, @Param("sysEnterpriseDingdingConfig") SysEnterpriseDingdingConfig sysEnterpriseDingdingConfig);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-24
     * @param: sysEnterpriseDingdingConfig
     * @return: java.util.List<com.jayud.auth.model.po.SysEnterpriseDingdingConfig>
     **/
    List<SysEnterpriseDingdingConfig> list(@Param("sysEnterpriseDingdingConfig") SysEnterpriseDingdingConfig sysEnterpriseDingdingConfig);


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
