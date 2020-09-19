package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.po.OrderPaymentCost;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单对应应付费用明细 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderPaymentCostMapper extends BaseMapper<OrderPaymentCost> {

}
