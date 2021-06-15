package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CounterOrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 柜子订单信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-15
 */
@Mapper
@Component
public interface CounterOrderInfoMapper extends BaseMapper<CounterOrderInfo> {

}
