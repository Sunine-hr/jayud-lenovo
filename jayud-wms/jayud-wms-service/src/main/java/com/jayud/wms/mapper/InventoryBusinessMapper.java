package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.InventoryBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存事务表 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface InventoryBusinessMapper extends BaseMapper<InventoryBusiness> {
    /**
    *   分页查询
    */
    IPage<InventoryBusiness> pageList(@Param("page") Page<InventoryBusiness> page, @Param("inventoryBusiness") InventoryBusiness inventoryBusiness);

    /**
    *   列表查询
    */
    List<InventoryBusiness> list(@Param("inventoryBusiness") InventoryBusiness inventoryBusiness);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryBusinessForExcel(Map<String, Object> paramMap);
}
