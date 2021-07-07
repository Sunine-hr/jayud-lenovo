package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderClearanceFile;
import com.jayud.mall.model.vo.OrderClearanceFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * 根据订单id，查询订单清关文件
     * @param orderId
     * @return
     */
    List<OrderClearanceFileVO> findOrderClearanceFileByOrderId(@Param("orderId") Long orderId);
}
