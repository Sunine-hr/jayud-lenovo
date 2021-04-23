package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.bo.StorageOutOrderForm;
import com.jayud.storage.model.po.StorageOutOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageOutOrderVO;

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

    StorageOutOrder getStorageOutOrderByMainOrderNO(String orderNo);

    StorageOutOrderVO getStorageOutOrderVOById(Long id);

    IPage<StorageInputOrderFormVO> findByPage(QueryStorageOrderForm form);
}
