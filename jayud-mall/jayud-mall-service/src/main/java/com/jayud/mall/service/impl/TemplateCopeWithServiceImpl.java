package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.TemplateCopeWithMapper;
import com.jayud.mall.model.bo.TemplateCopeWithForm;
import com.jayud.mall.model.po.TemplateCopeWith;
import com.jayud.mall.service.ITemplateCopeWithService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 报价对应应付费用明细 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class TemplateCopeWithServiceImpl extends ServiceImpl<TemplateCopeWithMapper, TemplateCopeWith> implements ITemplateCopeWithService {

    @Autowired
    TemplateCopeWithMapper templateCopeWithMapper;

    @Override
    public List<TemplateCopeWith> findTemplateCopeWith(TemplateCopeWithForm form) {
        Long id = form.getId();
        Integer qie = form.getQie();
        String costName = form.getCostName();
        String supplierCode = form.getSupplierCode();
        Integer calculateWay = form.getCalculateWay();
        Integer count = form.getCount();
        Integer unit = form.getUnit();
        Integer source = form.getSource();
        BigDecimal unitPrice = form.getUnitPrice();
        Integer cid = form.getCid();
        BigDecimal amount = form.getAmount();
        String remarks = form.getRemarks();
        QueryWrapper<TemplateCopeWith> queryWrapper = new QueryWrapper<>();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        if(qie != null){
            queryWrapper.eq("qie", qie);
        }
        if(costName != null && costName != ""){
            queryWrapper.like("cost_name", costName);
        }
        if(supplierCode != null && supplierCode != ""){
            queryWrapper.like("supplier_code", supplierCode);
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
        List<TemplateCopeWith> list = templateCopeWithMapper.selectList(queryWrapper);
        return list;
    }
}
