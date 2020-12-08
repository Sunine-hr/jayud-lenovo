package com.jayud.airfreight.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.AirProcessOptForm;
import com.jayud.airfreight.model.bo.QueryAirOrderForm;
import com.jayud.airfreight.model.po.AirBooking;
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

    /**
     * 更新流程状态
     */
    void updateProcessStatus(AirOrder airOrder, AirProcessOptForm form);

    /**
     * 空运流程操作记录
     */
    void airProcessOptRecord(AirProcessOptForm form);

    /**
     * 订舱操作
     */
    void doAirBookingOpt(AirProcessOptForm form);

    /**
     * 是否入仓
     */
    boolean isWarehousing(AirOrder airOrder);

    /**
     * 根据第三方唯一编码查询空运订单
     */
    public AirOrder getByThirdPartyOrderNo(String thirdPartyOrderNo);

    /**
     * 订舱驳回
     */
    void bookingRejected(AirOrder airOrder);
}
