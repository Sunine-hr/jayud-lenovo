package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderCopeReceivable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OrderCopeReceivableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    List<OrderCopeReceivableVO> findOrderCopeReceivableByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单id，提单id，查询订单应收费用信息
     * @param orderId 订单id
     * @param billId 提单id
     * @return
     */
    List<OrderCopeReceivableVO> findOrderCopeReceivableByOrderIdAndBillId(@Param("orderId") Long orderId, @Param("billId") Long billId);

    /**
     * 根据应收服务费用id，查询 订单费用
     * @param orderServiceReceivableId
     * @return
     */
    OrderCopeReceivableVO findOrderCopeReceivableByOrderServiceReceivableId(@Param("orderServiceReceivableId") Long orderServiceReceivableId);
}
