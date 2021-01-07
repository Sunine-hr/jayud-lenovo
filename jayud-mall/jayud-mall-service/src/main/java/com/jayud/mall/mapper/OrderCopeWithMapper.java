package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderCopeWith;
import com.jayud.mall.model.vo.OrderCopeWithVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单对应应付费用明细 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Mapper
@Component
public interface OrderCopeWithMapper extends BaseMapper<OrderCopeWith> {

    /**
     * 根据订单id，查询应付费用信息
     * @param orderId
     * @return
     */
    List<OrderCopeWithVO> findOrderCopeWithByOrderId(@Param("orderId") Long orderId);
}
