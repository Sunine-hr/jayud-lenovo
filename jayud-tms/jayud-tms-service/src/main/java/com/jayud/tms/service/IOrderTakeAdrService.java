package com.jayud.tms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.tms.model.vo.InputOrderTakeAdrVO;

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
     * 根据订单编号查询司机送货/收货地址
     */
    List<DriverOrderTakeAdrVO> getDriverOrderTakeAdr(List<String> orderNoList);
}
