package com.jayud.storage.service;

import com.jayud.storage.model.po.WarehouseGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.*;

import java.util.List;

/**
 * <p>
 * 仓储商品信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
public interface IWarehouseGoodsService extends IService<WarehouseGoods> {

    /**
     * 入库商品查询
     * @param id
     * @param orderNo
     * @return
     */
    List<WarehouseGoodsVO> getList(Long id, String orderNo);

    /**
     * 删除商品信息
     * @param orderId
     * @param orderNo
     * @return
     */
    void deleteWarehouseGoodsFormsByOrder(Long orderId, String orderNo);

    void deleteWarehouseGoodsFormsByOrderId(Long id);

    /**
     * 出库商品查询
     * @param id
     * @param orderNo
     * @return
     */
    List<WarehouseGoodsVO> getList1(Long id, String orderNo);

    /**
     * 获取出库操作记录
     * @param sku
     * @param locationCode
     * @return
     */
    List<OutGoodsOperationRecordFormVO> getListBySkuAndLocationCode(String sku, String locationCode,Long customerId);

    Integer getCount(String sku, String locationCode, Long customerId);

    List<OrderOutRecord> getListBySkuAndBatchNo(List<String> skuList, List<String> warehousingBatchNos);

    /**
     * 根据订单号与sku获取商品信息
     * @param sku
     * @param orderNo
     * @return
     */
    List<WarehouseGoods> getListBySkuAndOrderNo(String sku, String orderNo);

    List<WarehouseGoods> getOutListByOrderNo(String orderNo);

    /**
     * 根据批次号和订单号获取出库商品信息
     * @param warehousingBatchNo
     * @param orderNo
     * @return
     */
    List<WarehouseGoodsVO> getListByWarehousingBatchNoAndOrderNo(String warehousingBatchNo, String orderNo);



    List<OnShelfOrderVO> getListByOrderIdAndTime2(Long id, String orderNo, String startTime, String endTime);

    List<WarehouseGoods> getOutWarehouseGoodsByOrderNo(String orderNo);
}
