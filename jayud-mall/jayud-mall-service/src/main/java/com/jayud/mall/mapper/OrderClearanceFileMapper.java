package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderClearanceFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单对应清关文件 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-16
 */
@Mapper
@Component
public interface OrderClearanceFileMapper extends BaseMapper<OrderClearanceFile> {

}
