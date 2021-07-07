package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OrderCustomsFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OrderCustomsFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单对应报关文件 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-16
 */
@Mapper
@Component
public interface OrderCustomsFileMapper extends BaseMapper<OrderCustomsFile> {

    /**
     * 根据订单id，查询订单报关文件
     * @param orderId
     * @return
     */
    List<OrderCustomsFileVO> findOrderCustomsFileByOrderId(@Param("orderId") Long orderId);
}
