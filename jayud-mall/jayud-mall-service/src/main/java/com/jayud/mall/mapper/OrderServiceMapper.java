package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderService;
import com.jayud.mall.model.vo.OrderServiceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单服务表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Mapper
@Component
public interface OrderServiceMapper extends BaseMapper<OrderService> {

    /**
     * 根据订单id，查询list
     * @param orderId
     * @return
     */
    List<OrderServiceVO> findOrderServiceByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据id，查询服务
     * @param id
     * @return
     */
    OrderServiceVO findOrderServiceById(@Param("id") Long id);
}
