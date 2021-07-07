package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderServiceWith;
import com.jayud.mall.model.vo.OrderServiceWithVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单服务对应应付费用 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Mapper
@Component
public interface OrderServiceWithMapper extends BaseMapper<OrderServiceWith> {

    /**
     * 根据qie，查询
     * @param qie 订单服务id(order_service id)
     * @return
     */
    List<OrderServiceWithVO> findOrderServiceWithByQie(@Param("qie") Long qie);

    /**
     * 查询被删除的ids
     * @param qie 订单服务id(order_service id)
     * @param reserveOrderServiceWithIds
     * @return
     */
    List<Long> findOrderServiceWithByQieAndNotIds(@Param("qie") Long qie, @Param("reserveOrderServiceWithIds") List<Long> reserveOrderServiceWithIds);
}
