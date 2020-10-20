package com.jayud.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.OrderTransport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import com.jayud.tms.model.vo.OrderTransportVO;

/**
 * <p>
 * 中港运输订单 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderTransportService extends IService<OrderTransport> {


    /**
     * 创建订单
     * @param form
     * @return
     */
    boolean createOrderTransport(InputOrderTransportForm form);

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     * 获取订单详情
     * @param mainOrderNo
     * @return
     */
    public InputOrderTransportVO getOrderTransport(String mainOrderNo);

    /**
     * 中港分页查询
     * @param form
     * @return
     */
    IPage<OrderTransportVO> findTransportOrderByPage(QueryOrderTmsForm form);


}
