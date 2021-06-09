package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderCaseWms;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单装箱信息(仓库测量) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-09
 */
@Mapper
@Component
public interface OrderCaseWmsMapper extends BaseMapper<OrderCaseWms> {

}
