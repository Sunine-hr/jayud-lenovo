package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.StorageInputOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageInputOrderNumberVO;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.StorageInputOrderWarehouseingVO;

/**
 * <p>
 * 入库订单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
public interface IStorageInputOrderService extends IService<StorageInputOrder> {

    /**
     * 创建入库订单
     * @param storageInputOrderForm
     * @return
     */
    String createOrder(StorageInputOrderForm storageInputOrderForm);

    /**
     * 根据主订单号获取入库订单
     * @param orderNo
     * @return
     */
    StorageInputOrder getStorageInOrderByMainOrderNO(String orderNo);

    /**
     * 根据id获取入库订单数据
     * @param id
     * @return
     */
    StorageInputOrderVO getStorageInputOrderVOById(Long id);

    /**
     * 分页条件查询入库订单
     * @param form
     * @return
     */
    IPage<StorageInputOrderFormVO> findByPage(QueryStorageOrderForm form);

    /**
     * 仓储接单
     * @param form
     */
    void warehouseReceipt(StorageInProcessOptForm form);

    /**
     * 执行入库流程操作
     * @param form
     */
    void storageProcessOptRecord(StorageInProcessOptForm form);

    /**
     * 确认入仓
     * @param form
     */
    boolean confirmEntry(StorageInProcessOptForm form);

    /**
     * 分页获取仓储入库订单信息
     * @param form
     * @return
     */
    IPage<StorageInputOrderWarehouseingVO> findWarehousingByPage(QueryStorageOrderForm form);

    /**
     * 操作仓储入库流程
     * @param form
     * @return
     */
    boolean warehousingEntry(StorageInProcessOptForm form);

    /**
     * 驳回操作
     * @param tmp
     * @param auditInfoForm
     * @param storageInCargoRejected
     */
    void orderReceiving(StorageInputOrder tmp, AuditInfoForm auditInfoForm, StorageInCargoRejected storageInCargoRejected);

    /**
     * 查询入库订单记录
     * @param form
     * @return
     */
    IPage<StorageInputOrderNumberVO> findOrderRecordByPage(QueryInOrderForm form);
}
