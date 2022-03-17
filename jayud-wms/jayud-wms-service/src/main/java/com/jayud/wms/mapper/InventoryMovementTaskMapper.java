package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.InventoryMovementTask;
import com.jayud.wms.model.vo.InventoryMovementTaskAppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存移库任务 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-30
 */
@Mapper
public interface InventoryMovementTaskMapper extends BaseMapper<InventoryMovementTask> {
    /**
    *   分页查询
    */
    IPage<InventoryMovementTask> pageList(@Param("page") Page<InventoryMovementTask> page, @Param("inventoryMovementTask") InventoryMovementTask inventoryMovementTask);

    /**
    *   列表查询
    */
    List<InventoryMovementTask> list(@Param("inventoryMovementTask") InventoryMovementTask inventoryMovementTask);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryMovementTaskForExcel(Map<String, Object> paramMap);

    /**
     * 移库作业(汇总)查询
     * @param page
     * @param inventoryMovementTask
     * @return
     */
    IPage<InventoryMovementTaskAppVO> selectPageByFeign(@Param("page") Page<InventoryMovementTaskAppVO> page, @Param("inventoryMovementTask") InventoryMovementTask inventoryMovementTask);

    /**
     * 去移库(移库详情)
     * @param mainCode
     * @return
     */
    InventoryMovementTaskAppVO queryInventoryMovementTaskByMainCode(@Param("mainCode") String mainCode);
}
