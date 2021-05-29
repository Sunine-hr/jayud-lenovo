package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryOrderStorageForm;
import com.jayud.storage.model.po.StorageOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StorageOrderVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
public interface IStorageOrderService extends IService<StorageOrder> {

    IPage<StorageOrderVO> findByPage(QueryOrderStorageForm form);

    boolean saveStorageOrder(StorageOrder storageOrder);

    List<StorageOrderVO> getList(String orderNo, String outOrderNo, String customerName, String month);
}
