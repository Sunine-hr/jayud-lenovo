package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.InventoryCheckDetailPostForm;
import com.jayud.wms.model.po.InventoryCheckDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点明细表 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IInventoryCheckDetailService extends IService<InventoryCheckDetail> {

    /**
    *  分页查询
    * @param inventoryCheckDetail
    * @param req
    * @return
    */
    IPage<InventoryCheckDetail> selectPage(InventoryCheckDetail inventoryCheckDetail,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param inventoryCheckDetail
    * @return
    */
    List<InventoryCheckDetail> selectList(InventoryCheckDetail inventoryCheckDetail);

    /**
     * 保存(新增+编辑)
     * @param inventoryCheckDetail
     */
    InventoryCheckDetail saveOrUpdateInventoryCheckDetail(InventoryCheckDetail inventoryCheckDetail);

    /**
     * 逻辑删除
     * @param id
     */
    void delInventoryCheckDetail(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryCheckDetailForExcel(Map<String, Object> paramMap);

    /**
     * 盘点明细新增
     * @param inventoryCheckDetail
     * @return
     */
    InventoryCheckDetail add(InventoryCheckDetail inventoryCheckDetail);

    /**
     * 盘点明细确认过账(勾选n个)
     * @param form
     * @return
     */
    boolean confirmPost(InventoryCheckDetailPostForm form);

    /**
     * 查询明细数量
     * @param inventoryCheckIdList
     * @return
     */
    List<Map<String, Object>> queryDetailCountByInventoryCheckIds(List<Long> inventoryCheckIdList);

    /**
     * 盘点明细 已全部过账的盘点单
     * @return
     */
    List<Long> queryNotCheckIdList();


    /**
     * 确认盘点
     * @param form
     * @return
     */
    boolean affirmPost(InventoryCheckDetailPostForm form);

    /**
     * 撤回盘点
     * @param form
     * @return
     */
    boolean withdrawPost(InventoryCheckDetailPostForm form);

}
