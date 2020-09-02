package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.CustomerInfoVO;
import com.jayud.oms.mapper.CustomerInfoMapper;
import com.jayud.oms.service.ICustomerInfoService;
import org.springframework.stereotype.Service;


@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {


    @Override
    public CustomerInfoVO getCustomerInfoById(Long id) {
        CustomerInfo customerInfo = baseMapper.getCustomerInfoById(id);
        return  ConvertUtil.convert(customerInfo,CustomerInfoVO.class);
    }
}
