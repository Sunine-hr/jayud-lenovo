package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OrderCaseMapper;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.po.OrderCase;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单对应箱号信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OrderCaseServiceImpl extends ServiceImpl<OrderCaseMapper, OrderCase> implements IOrderCaseService {

    @Autowired
    OrderCaseMapper orderCaseMapper;

    @Override
    public IPage<OrderCaseVO> findOrderCaseByPage(QueryOrderCaseForm form) {
        //定义分页参数
        Page<OrderCaseVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderCaseVO> pageInfo = orderCaseMapper.findOrderCaseByPage(page, form);
        return pageInfo;

    }
}
