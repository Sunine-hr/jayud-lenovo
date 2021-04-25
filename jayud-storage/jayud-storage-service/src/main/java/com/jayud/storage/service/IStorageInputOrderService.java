package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.bo.StorageInProcessOptForm;
import com.jayud.storage.model.bo.StorageInputOrderForm;
import com.jayud.storage.model.po.StorageInputOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageInputOrderVO;

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

    StorageInputOrder getStorageInOrderByMainOrderNO(String orderNo);

    StorageInputOrderVO getStorageInputOrderVOById(Long id);

    IPage<StorageInputOrderFormVO> findByPage(QueryStorageOrderForm form);

    void warehouseReceipt(StorageInProcessOptForm form);

    void storageProcessOptRecord(StorageInProcessOptForm form);
}
