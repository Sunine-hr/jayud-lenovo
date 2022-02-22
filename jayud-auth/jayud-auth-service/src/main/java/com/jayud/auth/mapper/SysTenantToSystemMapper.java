package com.jayud.auth.mapper;

import com.jayud.auth.model.po.SysTenantToSystem;
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
 * 租户-系统关联表 Mapper 接口
 *
 * @author jayud
 * @since 2022-02-21
 */
@Mapper
public interface SysTenantToSystemMapper extends BaseMapper<SysTenantToSystem> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: page
     * @param: sysTenantToSystem
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysTenantToSystem>
     **/
    IPage<SysTenantToSystem> pageList(@Param("page") Page<SysTenantToSystem> page, @Param("sysTenantToSystem") SysTenantToSystem sysTenantToSystem);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysTenantToSystem
     * @return: java.util.List<com.jayud.auth.model.po.SysTenantToSystem>
     **/
    List<SysTenantToSystem> list(@Param("sysTenantToSystem") SysTenantToSystem sysTenantToSystem);


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

    /**
     * @description 删除关联数据
     * @author  ciro
     * @date   2022/2/22 13:31
     * @param: tenantId
     * @param: systemIdList
     * @param: username
     * @return: int
     **/
    int deletedRelation(@Param("tenantId") Long tenantId,@Param("systemIdList") List<String> systemIdList,@Param("username") String username);
}
