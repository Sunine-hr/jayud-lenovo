package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.FabWarehouseMapper;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.jayud.mall.service.IFabWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应收/FBA仓库 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class FabWarehouseServiceImpl extends ServiceImpl<FabWarehouseMapper, FabWarehouse> implements IFabWarehouseService {

    @Autowired
    FabWarehouseMapper fabWarehouseMapper;

    @Override
    public IPage<FabWarehouse> findFabWarehouseByPage(QueryFabWarehouseForm form) {
        //定义分页参数
        Page<FabWarehouse> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<FabWarehouse> pageInfo = fabWarehouseMapper.findFabWarehouseByPage(page, form);
        return pageInfo;
    }
}
