package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderCaseShop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单箱号对应商品信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-16
 */
@Mapper
@Component
public interface OrderCaseShopMapper extends BaseMapper<OrderCaseShop> {

}
