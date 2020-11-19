package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.OrderCaseMapper;
import com.jayud.mall.model.bo.CreateOrderCaseForm;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.po.OrderCase;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<OrderCaseVO> createOrderCaseList(CreateOrderCaseForm form) {
        Integer cartons = form.getCartons();// 总箱数
        BigDecimal weight = form.getWeight();// 每箱重量(KG)
        BigDecimal length = form.getLength();// 长(cm)
        BigDecimal width = form.getWidth();// 宽(cm)
        BigDecimal height = form.getHeight();// 高(cm)

        //体积(m3) = (长 * 宽 * 高) / 1000000
        BigDecimal volume = length.multiply(width).multiply(height).divide(new BigDecimal("1000000"));

        List<OrderCaseVO> list = new ArrayList<>();
        for(int i=0; i<cartons; i++) {
            OrderCaseVO orderCaseVO = new OrderCaseVO();
            orderCaseVO.setCartonNo("JYD-XD-" + i);//箱号生成规则，尝试一下雪花算法,生成唯一的id
            orderCaseVO.setAsnWeight(weight);
            orderCaseVO.setAsnLength(length);
            orderCaseVO.setAsnWidth(width);
            orderCaseVO.setAsnHeight(height);
            orderCaseVO.setAsnVolume(volume);
            list.add(orderCaseVO);
        }
        return list;
    }
}
