package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.InventoryAdjustmentForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.bo.InventoryMovementForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存明细表 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IInventoryDetailService extends IService<InventoryDetail> {

    /**
     * 物料库存查询
     * @param inventoryDetail
     * @param currentPage
     * @param pageSize
     * @param req
     * @return
     */
    IPage selectMaterialPage(InventoryDetail inventoryDetail, Integer currentPage, Integer pageSize, HttpServletRequest req);

    /**
     * 物料库存导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> exportInventoryMaterialForExcel(Map<String, Object> paramMap);

    /**
    *  分页查询
    * @param inventoryDetail
    * @param req
    * @return
    */
    IPage<InventoryDetail> selectPage(InventoryDetail inventoryDetail,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param inventoryDetail
    * @return
    */
    List<InventoryDetail> selectList(InventoryDetail inventoryDetail);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryDetailForExcel(Map<String, Object> paramMap);

    /**
     * 入库
     * @param list
     * @return
     */
    BaseResult input(List<InventoryDetailForm> list);

    /**
     * 出库分配
     * @param list
     * @return
     */
    BaseResult outputAllocation(List<InventoryDetailForm> list);

    /**
     * 库存调整
     * @param bo
     * @return
     */
    BaseResult adjustment(InventoryAdjustmentForm bo);

    /**
     * 库存移动
     * @param list
     * @return
     */
    BaseResult movement(List<InventoryMovementForm> list);

    /**
     * 撤销出库分配
     * @param list
     * @return
     */
    BaseResult cancelOutputAllocation(List<InventoryDetailForm> list);

    /**
     * 生成拣货
     * @param list
     * @return
     */
    BaseResult generatePicking(List<InventoryDetailForm> list);

    /**
     * 撤销拣货
     * @param list
     * @return
     */
    BaseResult cancelPicking(List<InventoryDetailForm> list);

    /**
     * 查询盘点
     * @param paramMap
     * @return
     */
    List<InventoryDetail> queryInventoryDetailForCheck(Map<String, Object> paramMap);

    /**
     * 出库
     * @param list
     * @return
     */
    BaseResult output(List<InventoryDetailForm> list);

    /**
     * 库存报表
     * @param queryShelfOrderTaskForm
     * @return
     */
    BaseResult selectInventoryReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm);


    /**
     * @description 出库分配
     * @author  ciro
     * @date   2022/4/6 13:41
     * @param: msg
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult outputAllocationByMsg(Map<Long, BigDecimal> msg);

    /**
     * @description 根据id集合取消分配
     * @author  ciro
     * @date   2022/4/6 17:26
     * @param: msg
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult canceloutputAllocationByIds(Map<Long,BigDecimal> msg);
}
