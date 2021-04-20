package com.jayud.storage.mapper;

import com.jayud.storage.model.po.GoodsLocationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
