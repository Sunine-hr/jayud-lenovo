package com.jayud.mall.mapper;

import com.jayud.mall.model.po.DeliveryAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提货地址基础数据表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Mapper
@Component
public interface DeliveryAddressMapper extends BaseMapper<DeliveryAddress> {

}
