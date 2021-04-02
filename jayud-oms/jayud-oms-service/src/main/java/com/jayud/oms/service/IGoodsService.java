package com.jayud.oms.service;

import com.jayud.oms.model.po.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 货品信息表 服务类
 * </p>
 *
 * @author LDR
 * @since 2020-12-16
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 查询商品信息
     */
    public List<Goods> getGoodsByBusIds(List<Long> busIds, Integer busType);

    void removeByOrderNo(String orderNo, Integer businessType);

    void removeByBusinessId(Long businessId, Integer businessType);

    List<Goods> getGoodsByBusOrders(List<String> orderNo, Integer businessType);

    void deleteGoodsByBusOrders(List<String> orderNo, Integer businessType);
}
