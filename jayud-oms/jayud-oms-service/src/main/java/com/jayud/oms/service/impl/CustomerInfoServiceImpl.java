package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.QueryCusAccountForm;
import com.jayud.model.bo.QueryCustomerInfoForm;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.AddCustomerInfoRelListVO;
import com.jayud.model.vo.CustAccountVO;
import com.jayud.model.vo.CustomerInfoVO;
import com.jayud.oms.mapper.CustomerInfoMapper;
import com.jayud.oms.service.ICustomerInfoService;
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
    public CustomerInfoVO getCustomerInfoById(QueryCustomerInfoForm form) {
        CustomerInfo customerInfo = baseMapper.getCustomerInfoById(form);
        return  ConvertUtil.convert(customerInfo,CustomerInfoVO.class);
    }

    @Override
    public AddCustomerInfoRelListVO getInfoBySave() {
        return null;
    }

    @Override
    public List<CustomerInfo> findCustomerInfoByCondition(Map<String,String> param) {
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
        page.addOrder(OrderItem.asc("ci.id"));
        IPage<CustAccountVO> pageInfo = baseMapper.findCustAccountByPage(page, form);
        return pageInfo;
    }


}
