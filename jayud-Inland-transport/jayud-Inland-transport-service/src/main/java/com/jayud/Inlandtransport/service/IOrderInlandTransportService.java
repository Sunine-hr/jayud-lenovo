package com.jayud.Inlandtransport.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.Inlandtransport.model.bo.AddOrderInlandTransportForm;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;
import org.springframework.transaction.annotation.Transactional;

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
    public void createOrder(AddOrderInlandTransportForm form);

    IPage<OrderInlandTransportFormVO> findByPage(QueryOrderForm form);
}
