package com.jayud.service.impl;

import com.jayud.model.po.OrderCustoms;
import com.jayud.mapper.OrderCustomsMapper;
import com.jayud.service.IOrderCustomsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报关业务订单表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderCustomsServiceImpl extends ServiceImpl<OrderCustomsMapper, OrderCustoms> implements IOrderCustomsService {

}
