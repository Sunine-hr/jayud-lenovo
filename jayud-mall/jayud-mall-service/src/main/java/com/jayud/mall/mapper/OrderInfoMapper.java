package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.model.vo.OrderInfoVO;
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
     * web端分页查询订单列表
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findWebOrderInfoByPage(Page<OrderInfoVO> page, @Param("form") QueryOrderInfoForm form);

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
}
