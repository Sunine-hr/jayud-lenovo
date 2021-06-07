package com.jayud.storage.mapper;

import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.InGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.InGoodsOperationRecordNumberVO;
import com.jayud.storage.model.vo.OnShelfOrderVO;
import com.jayud.storage.model.vo.QRCodeLocationGoodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 入库商品操作记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Mapper
public interface InGoodsOperationRecordMapper extends BaseMapper<InGoodsOperationRecord> {

    List<InGoodsOperationRecord> getListByOrderId(@Param("id") Long id);

    List<InGoodsOperationRecord> getListBySku(@Param("sku")String sku);

    List<InGoodsOperationRecordFormVO> getListBySkuAndLocationCode(@Param("sku")String sku, @Param("locationCode")String locationCode,@Param("customerId")Long customerId);

    InGoodsOperationRecordNumberVO getCountBySkuAndLocationCode(@Param("sku")String sku, @Param("locationCode")String locationCode);

    List<String> getWarehousingBatch(@Param("id")Long id);

    List<InGoodsOperationRecord> getListByAreaName(@Param("areaName")String areaName);

    List<QRCodeLocationGoodVO> getListByKuCode(@Param("kuCode")String kuCode);

    List<String> getWarehousingBatchNoComBox(@Param("kuCode")String kuCode, @Param("sku")String sku);

    List<OnShelfOrderVO> getListByOrderIdAndTime2(@Param("id")Long id, @Param("orderNo")String orderNo, @Param("startTime")String startTime, @Param("endTime")String endTime);
}
