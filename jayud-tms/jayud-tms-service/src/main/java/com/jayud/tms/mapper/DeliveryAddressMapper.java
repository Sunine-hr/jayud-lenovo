package com.jayud.tms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tms.model.po.DeliveryAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 提货地址基础数据表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Mapper
public interface DeliveryAddressMapper extends BaseMapper<DeliveryAddress> {

}
