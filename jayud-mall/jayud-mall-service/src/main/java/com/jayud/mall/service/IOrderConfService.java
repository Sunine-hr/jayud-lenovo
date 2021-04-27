package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderConfForm;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OrderConf;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OrderConfVO;

/**
 * <p>
 * 配载单 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface IOrderConfService extends IService<OrderConf> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form);

    /**
     * 保存配载单
     * @param form
     * @return
     */
    OrderConfVO saveOrderConf(OrderConfForm form);

    /**
     * 查看配载详情
     * @param id
     * @return
     */
    CommonResult<OrderConfVO> lookOrderConf(Long id);

    /**
     * 配载，关联报价
     * @param form
     * @return
     */
    OrderConfVO saveOrderConfByOfferInfo(OrderConfForm form);

    /**
     * 配载，关联提单
     * @param form
     * @return
     */
    OrderConfVO saveOrderConfByOceanBill(OrderConfForm form);

    /**
     * 新增编辑查询配载：提单、报价、运单(订单)
     * @param id
     * @return
     */
    OrderConfVO findOrderConfById(Long id);

    /**
     * 新增配载
     * @param form
     * @return
     */
    OrderConfVO addOrderConf(OrderConfForm form);
}
