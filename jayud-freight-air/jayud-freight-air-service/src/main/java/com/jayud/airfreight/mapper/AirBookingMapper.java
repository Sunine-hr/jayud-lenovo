package com.jayud.airfreight.mapper;

import com.jayud.airfreight.model.po.AirBooking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 空运订舱表 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2020-12-03
 */
@Mapper
public interface AirBookingMapper extends BaseMapper<AirBooking> {

}
