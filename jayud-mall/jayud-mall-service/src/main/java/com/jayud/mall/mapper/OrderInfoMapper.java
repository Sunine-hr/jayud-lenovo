package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.OrderInfoCustomerForm;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 产品订单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(Page<OrderInfoVO> page,@Param("form") QueryOrderInfoForm form);

    /**
     * 查询订单
     * @param id
     * @return
     */
    OrderInfoVO lookOrderInfoById(@Param("id") Long id);

    /**
     * 提交订单，查看订单详情
     * @param orderInfoId
     * @return
     */
    OrderInfoVO lookOrderInfo(@Param("orderInfoId") Long orderInfoId);

    /**
     * 新智慧，进入编辑订单<br/>
     * 根据报价id展示报价的信息，因为从新智慧过来的订单，没有关联上报价
     * @param orderInfoId 订单id
     * @param offerInfoId 报价id
     * @return
     */
    OrderInfoVO newLookOrderInfo(@Param("orderInfoId") Long orderInfoId, @Param("offerInfoId") Integer offerInfoId);

    /**
     * web端分页查询订单列表
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findWebOrderInfoByPage(Page<OrderInfoVO> page, @Param("form") QueryOrderInfoForm form);

    /**
     * web端分页查询订单列表(统计草稿)
     * @param form
     * @return
     */
    Long findOrderInfoDraftCount(@Param("form") QueryOrderInfoForm form);

    /**
     * 根据订单id，查询订单配载信息
     * @param orderId
     * @return
     */
    List<String> findOrderConfInfoByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单的运单id，查找配载，在查找配载单关联的提单
     * @param offerInfoId 报价id
     * @return
     */
    List<OceanBillVO> findOceanBillByOfferInfoId(@Param("offerInfoId") Integer offerInfoId);

    /**
     * 根据提单id，查找提单关联的柜号id list(其实是1对1的)
     * @param tdId
     * @return
     */
    List<OceanCounterVO> findOceanCounterByTdId(@Param("tdId") Long tdId);

    /**
     * 根据订单id，查询订单关联的任务，查看完成情况
     * @param orderId
     * @return
     */
    List<WaybillTaskRelevanceVO> findWaybillTaskRelevanceByOrderInfoId(@Param("orderId") Long orderId);

    /**
     * 查询订单，任务操作日志
     * @param id
     * @return
     */
    List<WaybillTaskRelevanceVO> lookOperateLog(@Param("id") Long id);

    /**
     * 查询客户订单
     * @param form
     * @return
     */
    List<OrderInfoVO> findOrderInfoByCustomer(@Param("form") OrderInfoCustomerForm form);

    /**
     * 根据订单号，查看订单
     * @param orderNo
     * @return
     */
    OrderInfoVO findOrderInfoByOrderNo(@Param("orderNo") String orderNo);
}
