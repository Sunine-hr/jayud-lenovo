package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.model.bo.QueryOrderInfoForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.IOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品订单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Override
    public IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderInfoVO> pageInfo = orderInfoMapper.findOrderInfoByPage(page, form);
        return pageInfo;
    }
}
