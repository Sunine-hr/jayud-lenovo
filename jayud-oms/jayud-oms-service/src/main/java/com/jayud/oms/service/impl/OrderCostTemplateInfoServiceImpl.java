package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.OrderCostTemplateInfo;
import com.jayud.oms.mapper.OrderCostTemplateInfoMapper;
import com.jayud.oms.service.IOrderCostTemplateInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 费用模板详情 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@Service
public class OrderCostTemplateInfoServiceImpl extends ServiceImpl<OrderCostTemplateInfoMapper, OrderCostTemplateInfo> implements IOrderCostTemplateInfoService {


    @Override
    public int deleteByTemplateId(Long templateId) {
        QueryWrapper<OrderCostTemplateInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderCostTemplateInfo::getOrderCostTemplateId, templateId);
        return this.getBaseMapper().delete(condition);
    }
}
