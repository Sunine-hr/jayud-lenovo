package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单服务表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Mapper
@Component
public interface OrderServiceMapper extends BaseMapper<OrderService> {

}
