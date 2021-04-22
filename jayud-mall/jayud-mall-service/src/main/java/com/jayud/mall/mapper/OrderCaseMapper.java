package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.po.OrderCase;
import com.jayud.mall.model.vo.OrderCaseConfVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单对应箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OrderCaseMapper extends BaseMapper<OrderCase> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<OrderCaseVO> findOrderCaseByPage(Page<OrderCaseVO> page, @Param("form") QueryOrderCaseForm form);

    /**
     * 根据订单id，查询订单箱号
     * @param orderId
     * @return
     */
    List<OrderCaseVO> findOrderCaseByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单id，查询订单箱号，以及订单箱号的配载信息（运单号->柜号->提单号->配载单号）
     * @param orderId
     * @return
     */
    List<OrderCaseConfVO> findOrderCaseConfByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询订单箱号
     * @param cartonNo 订单箱号
     * @return
     */
    OrderCaseVO findOrderCaseByCartonNo(@Param("cartonNo") String cartonNo);
}
