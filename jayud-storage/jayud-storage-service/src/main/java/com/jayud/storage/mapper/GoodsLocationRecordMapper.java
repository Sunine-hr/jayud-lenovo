package com.jayud.storage.mapper;

import com.jayud.storage.model.po.GoodsLocationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.StockLocationNumberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品对应库位表（记录入库的商品所在库位以及数量） Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Mapper
public interface GoodsLocationRecordMapper extends BaseMapper<GoodsLocationRecord> {

    List<GoodsLocationRecord> getListByGoodId(@Param("id") Long id,@Param("orderId") Long orderId,@Param("sku") String sku);

}
