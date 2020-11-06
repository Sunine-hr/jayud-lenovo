package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单对应箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OrderCaseMapper extends BaseMapper<OrderCase> {

}
