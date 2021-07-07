package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CounterOrderInfo;
import com.jayud.mall.model.vo.CounterOrderInfoExcelVO;
import com.jayud.mall.model.vo.CounterOrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 柜子订单信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-15
 */
@Mapper
@Component
public interface CounterOrderInfoMapper extends BaseMapper<CounterOrderInfo> {

    /**
     * 根据bid，查询
     * @param bId 柜子清单信息表(counter_list_info id)
     * @return
     */
    List<CounterOrderInfoVO> findCounterOrderInfoBybId(@Param("bId") Long bId);

    /**
     * 根据bid，查询导出数据
     * @param bid
     * @return
     */
    List<CounterOrderInfoExcelVO> findCounterOrderInfoExcelBybid(@Param("bid") Long bid);

    /**
     * 根据orderId，查询
     * @param orderId
     * @return
     */
    List<CounterOrderInfoVO> findCounterOrderInfoByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据orderId，查询留仓的箱子
     * @param orderId
     * @return
     */
    List<CounterOrderInfoVO> findCounterOrderInfoKeepWarehouseByOrderId(@Param("orderId") Long orderId);
}
