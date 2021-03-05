package com.jayud.Inlandtransport.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.model.bo.ProcessOptForm;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;

import java.util.List;

/**
 * <p>
 * 内陆订单 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
public interface IOrderInlandTransportService extends IService<OrderInlandTransport> {


    //创建订单
    public String createOrder(AddOrderInlandTransportForm form);

    IPage<OrderInlandTransportFormVO> findByPage(QueryOrderForm form);

    void updateProcessStatus(OrderInlandTransport orderInlandTransport, ProcessOptForm form);

    /**
     * 节点操作记录
     * @param form
     */
    public void processOptRecord(ProcessOptForm form);

    /**
     * 执行派车
     * @param form
     */
    void doDispatchOpt(ProcessOptForm form);

    List<OrderInlandTransport> getByCondition(OrderInlandTransport orderInlandTransport);
}
