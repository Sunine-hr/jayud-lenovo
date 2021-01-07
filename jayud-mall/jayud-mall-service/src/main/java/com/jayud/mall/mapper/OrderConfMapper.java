package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.model.vo.OrderConfVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 配载单 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface OrderConfMapper extends BaseMapper<OrderConf> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<OrderConfVO> findOrderConfByPage(Page<OrderConfVO> page,@Param("form") QueryOrderConfForm form);

    /**
     * 查询配载单
     * @param id
     */
    OrderConfVO findOrderConfById(@Param("id") Long id);

    /**
     * 根据配载单id，获取报价信息
     * @param orderId 配载单id
     * @return
     */
    List<OfferInfoVO> findOfferInfoVOByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据配载单id，获取提单柜号信息
     * @param orderId 配载单id
     * @return
     */
    List<OceanCounterVO> findOceanCounterVOByOrderId(@Param("orderId") Long orderId);
}
