package com.jayud.oms.mapper;

import com.jayud.oms.model.po.OrderTypeNumber;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单单号记录表 Mapper 接口
 * </p>
 *
 * @author llj
 * @since 2021-02-26
 */
@Mapper
public interface OrderTypeNumberMapper extends BaseMapper<OrderTypeNumber> {

    OrderTypeNumber getMaxNumberData(@Param("classCode")String classCode, @Param("date") String date);
}
