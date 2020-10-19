package com.jayud.tms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 中港运输订单 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderTransportMapper extends BaseMapper<OrderTransport> {

    /**
     * 获取订单详情
     * @param mainOrderNo
     * @return
     */
    public InputOrderTransportVO getOrderTransport(@Param("mainOrderNo") String mainOrderNo);
}
