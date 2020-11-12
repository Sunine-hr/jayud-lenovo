package com.jayud.finance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.po.CurrencyRate;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 币种汇率 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-12
 */
@Mapper
public interface CurrencyRateMapper extends BaseMapper<CurrencyRate> {

}
