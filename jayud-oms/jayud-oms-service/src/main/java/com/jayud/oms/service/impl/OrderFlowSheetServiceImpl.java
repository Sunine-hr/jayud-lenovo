package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.OrderFlowSheet;
import com.jayud.oms.mapper.OrderFlowSheetMapper;
import com.jayud.oms.service.IOrderFlowSheetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单完成情况表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-03-03
 */
@Service
public class OrderFlowSheetServiceImpl extends ServiceImpl<OrderFlowSheetMapper, OrderFlowSheet> implements IOrderFlowSheetService {

    /**
     * 根据条件搜索
     */
    @Override
    public List<OrderFlowSheet> getByCondition(OrderFlowSheet orderFlowSheet) {
        QueryWrapper<OrderFlowSheet> condition = new QueryWrapper<>(orderFlowSheet);
        return this.baseMapper.selectList(condition);
    }
}
