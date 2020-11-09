package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OrderConfMapper;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.OrderConfVO;
import com.jayud.mall.service.IOrderConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配载单 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class OrderConfServiceImpl extends ServiceImpl<OrderConfMapper, OrderConf> implements IOrderConfService {

    @Autowired
    OrderConfMapper orderConfMapper;

    @Override
    public IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form) {
        //定义分页参数
        Page<OrderConfVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderConfVO> pageInfo = orderConfMapper.findOrderConfByPage(page, form);
        return pageInfo;
    }
}
