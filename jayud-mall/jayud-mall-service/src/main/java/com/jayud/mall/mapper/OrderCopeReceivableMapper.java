package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderCopeReceivable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单对应应收费用明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Mapper
@Component
public interface OrderCopeReceivableMapper extends BaseMapper<OrderCopeReceivable> {

}
