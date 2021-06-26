package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 配载单 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface OrderConfMapper extends BaseMapper<OrderConf> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<OrderConfVO> findOrderConfByPage(Page<OrderConfVO> page,@Param("form") QueryOrderConfForm form);

    /**
     * 查询配载单
     * @param id
     */
    OrderConfVO findOrderConfById(@Param("id") Long id);

    /**
     * 根据配载单id，获取报价信息
     * @param orderId 配载单id
     * @return
     */
    List<OfferInfoVO> findOfferInfoVOByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据配载单id，获取提单柜号信息
     * @param orderId 配载单id
     * @return
     */
    List<OceanCounterVO> findOceanCounterVOByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据配载单id，获取提单信息
     * @param orderId
     * @return
     */
    List<OceanBillVO> findOceanBillVOByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据报价查询配载id，配载查询提单id，提单查询柜号id，最终获取柜号信息
     * @param offerInfoId
     * @return
     */
    List<OceanCounter> findOceanCounterByOfferInfoId(@Param("offerInfoId") Integer offerInfoId);

    /**
     * 查询配载订单下的箱子(配载 留仓)
     * @param orderId
     * @return
     */
    List<OrderCaseVO> findOrderCaseByOrderId(@Param("orderId") Long orderId);
}
