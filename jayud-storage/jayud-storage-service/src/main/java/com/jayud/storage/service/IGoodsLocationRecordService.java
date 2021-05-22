package com.jayud.storage.service;

import com.jayud.storage.model.po.GoodsLocationRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StockLocationNumberVO;

import java.util.List;

/**
 * <p>
 * 商品对应库位表（记录入库的商品所在库位以及数量） 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
public interface IGoodsLocationRecordService extends IService<GoodsLocationRecord> {

    List<GoodsLocationRecord> getGoodsLocationRecordByGoodId(Long id);

    List<GoodsLocationRecord> getListByGoodId(Long id, Long orderId, String sku);

    GoodsLocationRecord getListByGoodIdAndKuCode(Long inGoodId, String kuCode);

    GoodsLocationRecord getGoodsLocationRecordBySkuAndKuCode(String kuCode, String warehousingBatchNo, String sku);

    StockLocationNumberVO getListBySkuAndLocationCode(String sku, String locationCode,Long customerId);
}
