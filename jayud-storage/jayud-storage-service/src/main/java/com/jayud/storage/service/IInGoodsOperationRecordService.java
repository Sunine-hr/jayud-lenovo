package com.jayud.storage.service;

import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.*;

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

    List<InGoodsOperationRecord> getList(Long id, String orderNo,String sku);

    List<InGoodsOperationRecord> getListByWarehousingBatchNo(String warehousingBatchNo);

    List<InGoodsOperationRecord> getListByOrderId(Long id, String orderNo);

    List<InGoodsOperationRecord> getListByOrderId(Long id);

    List<InGoodsOperationRecord> getListBySku(String sku);

    InGoodsOperationRecord getListByWarehousingBatchNoAndSku(String warehousingBatchNo, String sku);

    /**
     * 入库操作记录表
     * @param sku
     * @param locationCode
     * @return
     */
    List<InGoodsOperationRecordFormVO> getListBySkuAndLocationCode(String sku, String locationCode,Long customerId);

    /**
     * 获取入库批次号下拉
     * @param id
     * @return
     */
    List<String> getWarehousingBatch(Long id);

    //获取分批次获取所有未出库数据
    List<InGoodsOperationRecord> getList1();

    List<InGoodsOperationRecord> getListByAreaName(String areaName);

    /**
     * 获取库位下所有商品信息
     * @param kuCode
     * @return
     */
    List<QRCodeLocationGoodVO> getListByKuCode(String kuCode);

    /**
     * 获取该库位下，该商品的批次号
     * @param kuCode
     * @param sku
     * @return
     */
    List<String> getWarehousingBatchNoComBox(String kuCode, String sku);


    /**
     * 获取批次号和订单号，获取订单入库商品信息
     * @param warehousingBatchNo
     * @param orderNo
     * @return
     */
    List<InGoodsOperationRecordVO> getListByWarehousingBatchNoAndOrderNo(String warehousingBatchNo, String orderNo);

    List<OnShelfOrderVO> getListByOrderIdAndTime2(Long id, String orderNo, String startTime, String endTime);
}
