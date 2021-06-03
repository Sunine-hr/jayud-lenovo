package com.jayud.storage.mapper;

import com.jayud.storage.model.po.WarehouseGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.OnShelfOrderVO;
import com.jayud.storage.model.vo.OrderOutRecord;
import com.jayud.storage.model.vo.OutGoodsOperationRecordFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仓储商品信息表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Mapper
public interface WarehouseGoodsMapper extends BaseMapper<WarehouseGoods> {

    Integer getCount(@Param("sku") String sku,@Param("locationCode")String locationCode,@Param("customerId")Long customerId);

    List<OutGoodsOperationRecordFormVO> getListBySkuAndLocationCode(@Param("sku")String sku, @Param("locationCode")String locationCode, @Param("customerId")Long customerId);

    List<OrderOutRecord> getListBySkuAndBatchNo(@Param("skuList")List<String> skuList, @Param("warehousingBatchNos")List<String> warehousingBatchNos);

    List<WarehouseGoods> getOutListByOrderNo(@Param("orderNo")String orderNo);

    List<OnShelfOrderVO> getListByOrderIdAndTime(@Param("id")Long id, @Param("orderNo")String orderNo, @Param("searchTime")String searchTime);

    List<OnShelfOrderVO> getListByOrderIdAndTime2(@Param("id")Long id, @Param("orderNo")String orderNo, @Param("startTime")String startTime, @Param("endTime")String endTime);
}
