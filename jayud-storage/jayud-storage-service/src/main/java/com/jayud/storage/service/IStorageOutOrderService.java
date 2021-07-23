package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.StorageOutOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.*;

import java.util.List;

/**
 * <p>
 * 出库订单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
public interface IStorageOutOrderService extends IService<StorageOutOrder> {

    /**
     * 创建出库订单
     * @param storageOutOrderForm
     * @return
     */
    String createOrder(StorageOutOrderForm storageOutOrderForm);

    /**
     * 根据主订单号获取出库订单
     * @param orderNo
     * @return
     */
    StorageOutOrder getStorageOutOrderByMainOrderNO(String orderNo);

    /**
     * 根据订单id获取出库订单信息
     * @param id
     * @return
     */
    StorageOutOrderVO getStorageOutOrderVOById(Long id);

    /**
     * 分页获取出库订单数据
     * @param form
     * @return
     */
    IPage<StorageOutOrderFormVO> findByPage(QueryStorageOrderForm form);

    /**
     * 处理驳回数据
     * @param tmp
     * @param auditInfoForm
     * @param storageOutCargoRejected
     */
    void orderReceiving(StorageOutOrder tmp, AuditInfoForm auditInfoForm, StorageOutCargoRejected storageOutCargoRejected);

    /**
     * 出库接单
     * @param form
     */
    void warehouseReceipt(StorageOutProcessOptForm form);


    void storageProcessOptRecord(StorageOutProcessOptForm form);

    /**
     * 打印拣货单
     * @param form
     * @return
     */
    boolean printPickingList(StorageOutProcessOptForm form);

    /**
     * 仓储拣货
     * @param form
     * @return
     */
    boolean warehousePicking(StorageOutProcessOptForm form);

    /**
     * 出仓异常
     * @param form
     */
    void abnormalOutOfWarehouse(StorageOutProcessOptForm form);

    /**
     * 确认出库
     * @param form
     */
    void confirmDelivery(StorageOutProcessOptForm form);

    /**
     * 根据条件获取拣货订单商品
     * @param form
     * @return
     */
    List<WarehouseGoodsLocationCodeVO> findByOrderNo(QueryPickUpGoodForm form);

    /**
     * PDA仓储拣货
     * @param form
     * @return
     */
    boolean PDAWarehousePicking(WarehousePickingForm form);


    List<OnShelfOrderVO> getListByQueryForm(QueryPutGoodForm form);

    List<StorageOutOrder> getByCondition(StorageOutOrder setMainOrderNo);

    List<StorageOutOrder> getOrdersByOrderNos(List<String> orderNos);
}
