package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderPickForm;
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

    /**
     * 分页查询订单提货信息
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPickVO> findOrderPickByPage(Page<OrderPickVO> page, @Param("form") QueryOrderPickForm form);

    /**
     * 根据transportId，查询提货信息
     * @param transportId
     * @return
     */
    List<OrderPickVO> findOrderPickByTransportId(@Param("transportId") Long transportId);
}
