package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OrderCaseShop;
import com.jayud.mall.model.vo.OrderCaseShopVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 订单箱号对应商品信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-16
 */
@Mapper
@Component
public interface OrderCaseShopMapper extends BaseMapper<OrderCaseShop> {

    /**
     * 查询订单商品装箱信息list
     * @param caseId
     * @return
     */
    List<OrderCaseShopVO> findOrderCaseShopByCaseId(@Param("caseId") Long caseId);
}
