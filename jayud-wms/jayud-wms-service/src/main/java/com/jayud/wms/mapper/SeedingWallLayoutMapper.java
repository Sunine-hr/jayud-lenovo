package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.SeedingWallLayout;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 播种位布局 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface SeedingWallLayoutMapper extends BaseMapper<SeedingWallLayout> {
    /**
    *   分页查询
    */
    IPage<SeedingWallLayout> pageList(@Param("page") Page<SeedingWallLayout> page, @Param("seedingWallLayout") SeedingWallLayout seedingWallLayout);

    /**
    *   列表查询
    */
    List<SeedingWallLayout> list(@Param("seedingWallLayout") SeedingWallLayout seedingWallLayout);
}
