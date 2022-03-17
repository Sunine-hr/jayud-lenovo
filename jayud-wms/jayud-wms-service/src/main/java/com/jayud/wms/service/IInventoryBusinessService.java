package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.InventoryBusiness;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存事务表 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IInventoryBusinessService extends IService<InventoryBusiness> {

    /**
    *  分页查询
    * @param inventoryBusiness
    * @param req
    * @return
    */
    IPage<InventoryBusiness> selectPage(InventoryBusiness inventoryBusiness,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param inventoryBusiness
    * @return
    */
    List<InventoryBusiness> selectList(InventoryBusiness inventoryBusiness);

    /**
     * 保存(新增+编辑)
     * @param inventoryBusiness
     */
    InventoryBusiness saveOrUpdateInventoryBusiness(InventoryBusiness inventoryBusiness);

    /**
     * 逻辑删除
     * @param id
     */
    void delInventoryBusiness(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryBusinessForExcel(Map<String, Object> paramMap);



}
