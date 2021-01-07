package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderPick;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OrderPickVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单对应提货信息表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Mapper
@Component
public interface OrderPickMapper extends BaseMapper<OrderPick> {

    /**
     * 根据订单id，查询提货信息
     * @param orderId
     * @return
     */
    List<OrderPickVO> findOrderPickByOrderId(@Param("orderId") Long orderId);
}
