package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.vo.InventoryReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存明细表 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface InventoryDetailMapper extends BaseMapper<InventoryDetail> {

    /**
     * 物料库存查询
     * @param page
     * @param inventoryDetail
     * @return
     */
    IPage<InventoryDetail> selectMaterialPage(@Param("page") Page<InventoryDetail> page, @Param("inventoryDetail") InventoryDetail inventoryDetail);

    /**
     * 物料库存导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> exportInventoryMaterialForExcel(Map<String, Object> paramMap);

    /**
    *   分页查询
    */
    IPage<InventoryDetail> pageList(@Param("page") Page<InventoryDetail> page, @Param("inventoryDetail") InventoryDetail inventoryDetail);

    /**
    *   列表查询
    */
    List<InventoryDetail> list(@Param("inventoryDetail") InventoryDetail inventoryDetail);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryDetailForExcel(Map<String, Object> paramMap);

    /**
     * 查询盘点
     * @param paramMap
     * @return
     */
    List<InventoryDetail> queryInventoryDetailForCheck(Map<String, Object> paramMap);

    /**
     * 根据货架库位信息 查询库位下 已经使用的库位容量（PS：这里获取的 库存明细的 可用量）
     * @param shelfMap
     * {                                                                      <br/>
            shelfMap.put("warehouseId", warehouseId);                         <br/>
            shelfMap.put("warehouseAreaId", warehouseAreaId);                 <br/>
            shelfMap.put("shelfId", shelfId);                                 <br/>
            shelfMap.put("warehouseLocationIds", warehouseLocationIds);       <br/>
     * }
     * @return
     */
    List<InventoryDetail> selectLocationCapacityByshelf(@Param("shelfMap") Map<String, Object> shelfMap);

    /**
     * 库存明细
     * @param queryShelfOrderTaskForm
     * @return
     */
    List<InventoryReportVO> selectInventoryReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm);

    /**
     * 根据货架库位信息 查询库位下 已经使用的库位物料数量
     * @param shelfMap
     * {                                                                      <br/>
            shelfMap.put("warehouseId", warehouseId);                         <br/>
            shelfMap.put("warehouseAreaId", warehouseAreaId);                 <br/>
            shelfMap.put("shelfId", shelfId);                                 <br/>
            shelfMap.put("warehouseLocationId", warehouseLocationId);         <br/>
     * }
     * @return
     */
    List<InventoryDetail> selectWarehouseLocationByshelf(@Param("shelfMap") Map<String, Object> shelfMap);
}
