package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.OrderCaseQueryForm;
import com.jayud.mall.model.po.CounterCaseInfo;
import com.jayud.mall.model.vo.CounterCaseInfoVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 柜子箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
@Mapper
@Component
public interface CounterCaseInfoMapper extends BaseMapper<CounterCaseInfo> {

    /**
     * 查询 柜子(订单)箱号信息
     * @param bId 柜子清单信息表(counter_list_info id)
     * @param orderId 订单id(order_info id)
     * @return
     */
    List<CounterCaseInfoVO> findCounterCaseInfoByBidAndOrderId(@Param("bId") Long bId, @Param("orderId") Long orderId);

    /**
     * 查询-未选择的箱子(运单-绑定装柜箱子)
     * @param form
     * @return
     */
    List<OrderCaseVO> findUnselectedOrderCase(@Param("form") OrderCaseQueryForm form);

    /**
     * 查询-已选择的箱子(运单-绑定装柜箱子)
     * @param form
     * @return
     */
    List<OrderCaseVO> findSelectedOrderCase(@Param("form") OrderCaseQueryForm form);

    /**
     * 查询 柜子(订单)箱号信息
     * @param orderId 订单id(order_info id)
     * @return
     */
    List<CounterCaseInfoVO> findCounterCaseInfoByOrderId(@Param("orderId") Long orderId);
}
