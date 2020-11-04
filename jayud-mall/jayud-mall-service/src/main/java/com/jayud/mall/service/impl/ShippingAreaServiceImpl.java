package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryShippingAreaForm;
import com.jayud.mall.model.po.ShippingArea;
import com.jayud.mall.mapper.ShippingAreaMapper;
import com.jayud.mall.service.IShippingAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 集货仓表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class ShippingAreaServiceImpl extends ServiceImpl<ShippingAreaMapper, ShippingArea> implements IShippingAreaService {

    @Autowired
    ShippingAreaMapper shippingAreaMapper;

    @Override
    public IPage<ShippingArea> findShippingAreaByPage(QueryShippingAreaForm form) {
        //定义分页参数
        Page<ShippingArea> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<ShippingArea> pageInfo = shippingAreaMapper.findShippingAreaByPage(page, form);
        return pageInfo;
    }
}
