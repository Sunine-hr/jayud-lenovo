package com.jayud.storage.service;

import com.jayud.storage.model.bo.StorageFastOrderForm;
import com.jayud.storage.model.po.StorageFastOrder;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
