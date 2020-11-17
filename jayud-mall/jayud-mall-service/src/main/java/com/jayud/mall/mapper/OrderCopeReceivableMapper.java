package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderCopeReceivable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OrderCopeReceivableVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * 根据订单id，查询订单应收费用信息
     * @param orderId
     * @return
     */
    List<OrderCopeReceivableVO> findOrderCopeReceivableByOrderId(Long orderId);
}
