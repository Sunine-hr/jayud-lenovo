package com.jayud.Inlandtransport.service;

import cn.hutool.db.sql.Order;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.SendCarPdfVO;

import java.util.List;

/**
 * <p>
 * 内陆派车信息 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-03-04
 */
public interface IOrderInlandSendCarsService extends IService<OrderInlandSendCars> {

    SendCarPdfVO initPdfData(OrderInlandTransport order, String type);

    public List<OrderInlandSendCars> getByCondition(OrderInlandSendCars orderInlandSendCars);
}
