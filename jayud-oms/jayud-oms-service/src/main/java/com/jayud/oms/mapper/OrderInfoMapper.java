package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.model.po.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 主订单基础数据表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

}
