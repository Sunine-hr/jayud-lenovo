package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.model.bo.QueryCustomerInfoForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.bo.QueryCusAccountForm;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.model.vo.CustAccountVO;
import com.jayud.oms.mapper.CustomerInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {


    @Override
    public IPage<CustomerInfoVO> findCustomerInfoByPage(QueryCustomerInfoForm form) {
        //定义分页参数
        Page<CustomerInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("ci.id"));
        IPage<CustomerInfoVO> pageInfo = baseMapper.findCustomerInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public CustomerInfoVO getCustomerInfoById(Long id) {
        return  baseMapper.getCustomerInfoById(id);
    }

    @Override
    public List<CustomerInfo> findCustomerInfoByCondition(Map<String,Object> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","1");
        for(String key : param.keySet()){
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key,value);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CustAccountVO getCustAccountByCondition(Map<String, Object> param) {
        return baseMapper.getCustAccountByCondition(param);
    }

    @Override
    public IPage<CustAccountVO> findCustAccountByPage(QueryCusAccountForm form) {
        //定义分页参数
        Page<CustomerInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("su.id"));
        IPage<CustAccountVO> pageInfo = baseMapper.findCustAccountByPage(page, form);
        return pageInfo;
    }


}
