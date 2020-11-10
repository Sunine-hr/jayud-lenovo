package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CurrencyRate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 币种汇率 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Mapper
@Component
public interface CurrencyRateMapper extends BaseMapper<CurrencyRate> {

}
