package com.jayud.storage.service;

import com.jayud.storage.model.bo.StorageInputOrderForm;
import com.jayud.storage.model.po.StorageInputOrder;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
