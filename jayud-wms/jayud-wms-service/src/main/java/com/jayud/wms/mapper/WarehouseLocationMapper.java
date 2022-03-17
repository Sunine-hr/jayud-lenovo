package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryInventoryForm;
import com.jayud.wms.model.bo.QueryWarehouseLocationForm;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库位信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-14
 */
@Mapper
public interface WarehouseLocationMapper extends BaseMapper<WarehouseLocation> {
    /**
     *   分页查询
     */
    IPage<WarehouseLocationVO> pageList(@Param("page") Page<WarehouseLocationVO> page, @Param("warehouseLocation") QueryWarehouseLocationForm warehouseLocation);

    /**
     *   列表查询
     */
    List<WarehouseLocationVO> list(@Param("warehouseLocation") WarehouseLocation warehouseLocation);

    List<LinkedHashMap<String, Object>> queryWarehouseLocationForExcel(Map<String, Object> paramMap);

    /**
     * 入库选择空库位list
     * @param form
     * @return
     */
    List<WarehouseLocationVO> queryWarehouseLocation(@Param("form") QueryInventoryForm form);

    /**
     * 根据货架信息 查询货架下 所有库位的容量
     * @param shelfMap
     * {                                                           <br/>
            shelfMap.put("warehouseId", warehouseId);              <br/>
            shelfMap.put("warehouseAreaId", warehouseAreaId);      <br/>
            shelfMap.put("shelfId", shelfId);                      <br/>
     * }                                                           <br/>
     * @return
     */
    List<WarehouseLocation> selectLocationCapacityByShelfId(@Param("shelfMap") Map<String, Object> shelfMap);
}