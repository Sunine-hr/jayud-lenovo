package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.ShelfStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 上架策略 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-13
 */
@Mapper
public interface ShelfStrategyMapper extends BaseMapper<ShelfStrategy> {
    /**
    *   分页查询
    */
    IPage<ShelfStrategy> pageList(@Param("page") Page<ShelfStrategy> page, @Param("shelfStrategy") ShelfStrategy shelfStrategy);

    /**
    *   列表查询
    */
    List<ShelfStrategy> list(@Param("shelfStrategy") ShelfStrategy shelfStrategy);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfStrategyForExcel(Map<String, Object> paramMap);
}
