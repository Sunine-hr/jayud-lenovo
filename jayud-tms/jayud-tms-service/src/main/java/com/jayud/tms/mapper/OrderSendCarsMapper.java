package com.jayud.tms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.vo.OrderSendCarsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单派车信息 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderSendCarsMapper extends BaseMapper<OrderSendCars> {

    /**
     * 获取派车及仓库信息
     * @param orderNo
     * @return
     */
    OrderSendCarsVO getOrderSendInfo(@Param("orderNo") String orderNo);
}
