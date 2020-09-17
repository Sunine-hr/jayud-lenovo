package com.jayud.oms.mapper;

import com.jayud.oms.model.po.OrderTransportHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 子订单变量历史记录表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderTransportHistoryMapper extends BaseMapper<OrderTransportHistory> {

}
