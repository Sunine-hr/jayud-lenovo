package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderShop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OrderShopVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单对应商品 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-16
 */
@Mapper
@Component
public interface OrderShopMapper extends BaseMapper<OrderShop> {

    /**
     * 根据订单id，获取订单商品
     * @param orderId
     * @return
     */
    List<OrderShopVO> findOrderShopByOrderId(@Param("orderId") Long orderId);
}
