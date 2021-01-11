package com.jayud.oms.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.OrderTakeAdr;
import com.jayud.oms.model.vo.InputOrderTakeAdrVO;

import java.util.List;

/**
 * <p>
 * 订单对应收货地址 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderTakeAdrService extends IService<OrderTakeAdr> {


    /**
     * 获取提货信息
     *
     * @param orderNo
     * @return
     */
    public List<InputOrderTakeAdrVO> findTakeGoodsInfo(String orderNo);

    /**
     * 获取送货地址数量
     */
    int getDeliveryAddressNum(String orderNo);

    /**
     * 根据订单编号获取地址
     */
    List<OrderTakeAdr> getOrderTakeAdrByOrderNo(String orderNo,Integer oprType);


}
