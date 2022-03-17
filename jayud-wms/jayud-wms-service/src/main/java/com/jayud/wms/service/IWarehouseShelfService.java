package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WarehouseShelf;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 货架信息 服务类
 *
 * @author jyd
 * @since 2022-03-05
 */
public interface IWarehouseShelfService extends IService<WarehouseShelf> {

    /**
    *  分页查询
    * @param warehouseShelf
    * @param req
    * @return
    */
    IPage<WarehouseShelf> selectPage(WarehouseShelf warehouseShelf,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param warehouseShelf
    * @return
    */
    List<WarehouseShelf> selectList(WarehouseShelf warehouseShelf);

    /**
     * 保存(新增+编辑)
     * @param warehouseShelf
     */
    WarehouseShelf saveOrUpdateWarehouseShelf(WarehouseShelf warehouseShelf);

    /**
     * 逻辑删除
     * @param id
     */
    void delWarehouseShelf(Long id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWarehouseShelfForExcel(Map<String, Object> paramMap);

    /**
     * 根据编码、仓库id、库位id获取货架
     * @param code
     * @return
     */
    WarehouseShelf getWarehouseShelfBycode(String code);

    /**
     * 根据库区获取仓库货架信息
     * @param id
     * @return
     */
    List<WarehouseShelf> getDataByWarehouseAreaId(Long id);
}
