package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.InventoryCheckDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点明细表 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface InventoryCheckDetailMapper extends BaseMapper<InventoryCheckDetail> {
    /**
    *   分页查询
    */
    IPage<InventoryCheckDetail> pageList(@Param("page") Page<InventoryCheckDetail> page, @Param("inventoryCheckDetail") InventoryCheckDetail inventoryCheckDetail);

    /**
    *   列表查询
    */
    List<InventoryCheckDetail> list(@Param("inventoryCheckDetail") InventoryCheckDetail inventoryCheckDetail);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryCheckDetailForExcel(Map<String, Object> paramMap);

    /**
     * 查询明细数量
     * @param inventoryCheckIds
     * @return
     */
    List<Map<String, Object>> queryDetailCountByInventoryCheckIds(@Param("inventoryCheckIds") List<Long> inventoryCheckIds);

    /**
     * 盘点明细 已全部过账的盘点单
     * @return
     */
    List<Long> queryNotCheckIdList();
}
