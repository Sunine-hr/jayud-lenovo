package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryInventoryForm;
import com.jayud.wms.model.bo.QueryWarehouseLocationForm;
import com.jayud.wms.model.bo.WarehouseLocationForm;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库位信息 服务类
 *
 * @author jyd
 * @since 2021-12-14
 */
public interface IWarehouseLocationService extends IService<WarehouseLocation> {

    /**
     *  分页查询
     * @param warehouseLocation
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<WarehouseLocationVO> selectPage(QueryWarehouseLocationForm warehouseLocation,
                                          Integer pageNo,
                                          Integer pageSize,
                                          HttpServletRequest req);

    /**
     *  查询列表
     * @param warehouseLocation
     * @return
     */
    List<WarehouseLocationVO> selectList(WarehouseLocation warehouseLocation);

    WarehouseLocation getWarehouseLocationByCode(String code, Long warehouseId, Long warehouseAreaId);

    BaseResult saveOrUpdateWarehouseLocation(WarehouseLocationForm warehouseLocation);

    void deleteById(List<Long> ids);

    List<WarehouseLocation> getWarehouseLocationByWarehouseAreaId(Long id);

    List<LinkedHashMap<String, Object>> queryWarehouseLocationForExcel(Map<String, Object> paramMap);

    /**
     * 入库选择空库位list
     * @param form
     * @return
     */
    List<WarehouseLocationVO> queryWarehouseLocation(QueryInventoryForm form);

    /**
     * 根据货架id获取库位信息
     * @param id
     * @return
     */
    List<WarehouseLocation> getWarehouseLocationByWarehouseShelfId(Long id);

    /**
     * 根据编码和启用状态查询库位信息
     * @param code
     * @return
     */
    WarehouseLocation getWarehouseLocationByCodeAndStatus(String code);
}
