package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OrderCaseWmsMapper;
import com.jayud.mall.model.bo.QueryOrderCaseWmsForm;
import com.jayud.mall.model.po.OrderCaseWms;
import com.jayud.mall.model.vo.OrderCaseWmsVO;
import com.jayud.mall.service.IOrderCaseWmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单装箱信息(仓库测量) 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-09
 */
@Service
public class OrderCaseWmsServiceImpl extends ServiceImpl<OrderCaseWmsMapper, OrderCaseWms> implements IOrderCaseWmsService {

    @Autowired
    OrderCaseWmsMapper orderCaseWmsMapper;

    @Override
    public IPage<OrderCaseWmsVO> findOrderCaseWmsPage(QueryOrderCaseWmsForm form) {
        //定义分页参数
        Page<OrderCaseWmsVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.create_time"));
        IPage<OrderCaseWmsVO> pageInfo = orderCaseWmsMapper.findOrderCaseWmsPage(page, form);
        return pageInfo;
    }

    @Override
    public OrderCaseWmsVO findOrderCaseWmsByCartonNo(String cartonNo) {
        OrderCaseWmsVO orderCaseWmsByCartonNo = orderCaseWmsMapper.findOrderCaseWmsByCartonNo(cartonNo);
        return orderCaseWmsByCartonNo;
    }
}
