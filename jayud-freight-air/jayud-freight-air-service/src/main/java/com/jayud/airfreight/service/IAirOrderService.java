package com.jayud.airfreight.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.QueryAirOrderForm;
import com.jayud.airfreight.model.po.AirOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.airfreight.model.vo.AirOrderFormVO;

import java.util.List;

/**
 * <p>
 * 空运订单表 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
public interface IAirOrderService extends IService<AirOrder> {

    /**
     * 创建订单
     */
    void createOrder(AddAirOrderForm addAirOrderForm);

    /**
     * 生成订单号
     */
    String generationOrderNo();

    /**
     * 是否存在订单
     */
    boolean isExistOrder(String orderNo);

    /**
     * 分页查询空运订单信息
     */
    IPage<AirOrderFormVO> findByPage(QueryAirOrderForm form);
}
