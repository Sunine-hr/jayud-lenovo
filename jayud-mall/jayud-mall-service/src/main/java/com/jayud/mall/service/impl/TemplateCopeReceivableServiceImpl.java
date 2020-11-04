package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.mall.model.bo.TemplateCopeReceivableForm;
import com.jayud.mall.model.po.TemplateCopeReceivable;
import com.jayud.mall.mapper.TemplateCopeReceivableMapper;
import com.jayud.mall.service.ITemplateCopeReceivableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 报价对应应收费用明细 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class TemplateCopeReceivableServiceImpl extends ServiceImpl<TemplateCopeReceivableMapper, TemplateCopeReceivable> implements ITemplateCopeReceivableService {

    @Autowired
    TemplateCopeReceivableMapper templateCopeReceivableMapper;

    @Override
    public List<TemplateCopeReceivable> findTemplateCopeReceivable(TemplateCopeReceivableForm form) {
        Long id = form.getId();
        Integer qie = form.getQie();
        String costName = form.getCostName();
        String specificationCode = form.getSpecificationCode();
        Integer calculateWay = form.getCalculateWay();
        Integer count = form.getCount();
        Integer unit = form.getUnit();
        Integer source = form.getSource();
        BigDecimal unitPrice = form.getUnitPrice();
        Integer cid = form.getCid();
        BigDecimal amount = form.getAmount();
        String remarks = form.getRemarks();
        QueryWrapper<TemplateCopeReceivable> queryWrapper = new QueryWrapper<>();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        if(qie != null){
            queryWrapper.eq("qie", qie);
        }
        if(costName != null && costName != ""){
            queryWrapper.like("cost_name", costName);
        }
        if(specificationCode != null && specificationCode != ""){
            queryWrapper.like("specification_code", specificationCode);
        }
        if(calculateWay != null){
            queryWrapper.eq("calculate_way", calculateWay);
        }
        if(count != null){
            queryWrapper.eq("count", count);
        }
        if(unit != null){
            queryWrapper.eq("unit", unit);
        }
        if(source != null){
            queryWrapper.eq("source", source);
        }
        if(unitPrice != null){
            queryWrapper.eq("unit_price", unitPrice);
        }
        if(cid != null){
            queryWrapper.eq("cid", cid);
        }
        if(amount != null){
            queryWrapper.eq("amount", amount);
        }
        if(remarks != null && remarks != ""){
            queryWrapper.like("remarks", remarks);
        }
        List<TemplateCopeReceivable> list = templateCopeReceivableMapper.selectList(queryWrapper);
        return list;
    }
}
