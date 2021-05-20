package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderInteriorStatus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单内部状态表(非流程状态) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-20
 */
@Mapper
@Component
public interface OrderInteriorStatusMapper extends BaseMapper<OrderInteriorStatus> {

}
