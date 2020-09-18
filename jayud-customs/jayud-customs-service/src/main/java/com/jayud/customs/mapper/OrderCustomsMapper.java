package com.jayud.customs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.OrderCustomsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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


    List<OrderCustomsVO> findOrderCustomsByCondition(Map<String, Object> param);
}
