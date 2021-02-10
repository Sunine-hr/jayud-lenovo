package com.jayud.oceanship.mapper;

import com.jayud.oceanship.po.OrderStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行) Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-01-30
 */
@Mapper
public interface OrderStatusMapper extends BaseMapper<OrderStatus> {

}
