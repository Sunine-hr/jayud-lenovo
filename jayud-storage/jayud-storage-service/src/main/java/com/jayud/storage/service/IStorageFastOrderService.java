package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.StorageFastOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StorageFastOrderFormVO;
import com.jayud.storage.model.vo.StorageFastOrderVO;

/**
 * <p>
 * 快进快出订单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
public interface IStorageFastOrderService extends IService<StorageFastOrder> {

    String createOrder(StorageFastOrderForm inputStorageFastOrderForm);

    StorageFastOrder getStorageFastOrderByMainOrderNO(String orderNo);

    StorageFastOrderVO getStorageFastOrderVOById(Long id);

    IPage<StorageFastOrderFormVO> findByPage(QueryStorageFastOrderForm form);

    //驳回
    void orderReceiving(StorageFastOrder tmp, AuditInfoForm auditInfoForm, StorageOutCargoRejected storageOutCargoRejected);

    //快进快出不入库接单
    void ordersReceived(StorageFastProcessOptForm form);

    //操作流程记录
    void storageProcessOptRecord(StorageFastProcessOptForm form);

    //快进快出接单
    void confirmReceipt(StorageFastProcessOptForm form);

    StorageFastOrder getStorageFastOrderByOrderNO(String orderNo);
}
