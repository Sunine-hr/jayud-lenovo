package com.jayud.mall.mapper;

import com.jayud.mall.model.po.TradingRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 交易记录表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
@Mapper
@Component
public interface TradingRecordMapper extends BaseMapper<TradingRecord> {

}
