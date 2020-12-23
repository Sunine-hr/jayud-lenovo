package com.jayud.tms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.vo.DriverInfoPdfVO;
import com.jayud.tms.model.vo.OrderSendCarsVO;
import com.jayud.tms.model.vo.SendCarListTempVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 获取司机资料PDF数据
     * @param orderNo
     * @return
     */
    DriverInfoPdfVO initDriverInfo(@Param("orderNo") String orderNo);

    /**
     * 初始化派车单
     * @param orderNo
     * @return
     */
    List<SendCarListTempVO> initSendCarList(@Param("orderNo") String orderNo);
}
