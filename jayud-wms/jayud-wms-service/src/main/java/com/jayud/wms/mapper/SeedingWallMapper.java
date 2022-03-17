package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.SeedingWall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 播种墙信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface SeedingWallMapper extends BaseMapper<SeedingWall> {
    /**
    *   分页查询
    */
    IPage<SeedingWall> pageList(@Param("page") Page<SeedingWall> page, @Param("seedingWall") SeedingWall seedingWall);

    /**
    *   列表查询
    */
    List<SeedingWall> list(@Param("seedingWall") SeedingWall seedingWall);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> querySeedingWallForExcel(Map<String, Object> paramMap);
}
