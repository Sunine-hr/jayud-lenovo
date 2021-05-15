package com.jayud.storage.service;

import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库商品操作记录表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
public interface IInGoodsOperationRecordService extends IService<InGoodsOperationRecord> {

    List<InGoodsOperationRecord> getList(Long id, String orderNo,String name);

    List<InGoodsOperationRecord> getListByWarehousingBatchNo(String warehousingBatchNo);

    List<InGoodsOperationRecord> getListByOrderId(Long id, String orderNo);

    List<InGoodsOperationRecord> getListByOrderId(Long id);

    List<InGoodsOperationRecord> getListBySku(String sku);
}
