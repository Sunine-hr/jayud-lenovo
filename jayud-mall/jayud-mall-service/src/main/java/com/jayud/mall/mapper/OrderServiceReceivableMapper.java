package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderServiceReceivable;
import com.jayud.mall.model.vo.OrderServiceReceivableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单服务对应应收费用 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Mapper
@Component
public interface OrderServiceReceivableMapper extends BaseMapper<OrderServiceReceivable> {

    /**
     * 根据qie,查询
     * @param qie 订单服务id(order_service id)
     * @return
     */
    List<OrderServiceReceivableVO> findOrderServiceReceivableByQie(@Param("qie") Long qie);

    /**
     * 查询被删除的ids
     * @param qie 订单服务id(order_service id)
     * @param reserveOrderServiceReceivableIds  被保留的ids
     * @return
     */
    List<Long> findOrderServiceReceivableByQieAndNotIds(@Param("qie") Long qie, @Param("reserveOrderServiceReceivableIds") List<Long> reserveOrderServiceReceivableIds);
}
