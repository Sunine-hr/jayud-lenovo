package com.jayud.storage.service;

import com.jayud.storage.model.bo.WarehousePickingForm;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.GoodsLocationRecordFormVO;
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

    //入库库位
    List<GoodsLocationRecord> getGoodsLocationRecordByGoodId(Long id);

    List<GoodsLocationRecord> getListByGoodId(String warehousingBatchNo, String sku);

    //入库库位
    GoodsLocationRecord getGoodsLocationRecordBySkuAndKuCode(String kuCode, String warehousingBatchNo, String sku);

    StockLocationNumberVO getListBySkuAndLocationCode(String sku, String locationCode,Long customerId);

    //出库
    List<GoodsLocationRecordFormVO> getOutGoodsLocationRecordByGoodId(Long id);

    //根据库位和id查询库位信息
    GoodsLocationRecord getInListByKuCodeAndInGoodId(String kuCode, Long id);

    //获取未拣货的数据
    List<GoodsLocationRecordFormVO> getOutGoodsLocationRecordByGoodIdAndPicked(Long id);

    //出库 根据库位和id查询库位信息
    GoodsLocationRecord getOutListByKuCodeAndInGoodId(Long id, String kuCode);
}
