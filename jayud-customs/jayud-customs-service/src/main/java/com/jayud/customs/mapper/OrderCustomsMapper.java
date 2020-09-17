package com.jayud.customs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.customs.model.po.OrderCustoms;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 报关业务订单表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderCustomsMapper extends BaseMapper<OrderCustoms> {

}
