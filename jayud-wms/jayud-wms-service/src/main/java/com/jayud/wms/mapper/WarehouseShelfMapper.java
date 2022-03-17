package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WarehouseShelf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 货架信息 Mapper 接口
 *
 * @author jyd
 * @since 2022-03-05
 */
@Mapper
public interface WarehouseShelfMapper extends BaseMapper<WarehouseShelf> {
    /**
    *   分页查询
    */
    IPage<WarehouseShelf> pageList(@Param("page") Page<WarehouseShelf> page, @Param("warehouseShelf") WarehouseShelf warehouseShelf);

    /**
    *   列表查询
    */
    List<WarehouseShelf> list(@Param("warehouseShelf") WarehouseShelf warehouseShelf);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWarehouseShelfForExcel(Map<String, Object> paramMap);
}
