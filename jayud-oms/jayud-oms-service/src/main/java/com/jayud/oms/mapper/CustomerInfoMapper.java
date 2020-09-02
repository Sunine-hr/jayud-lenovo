package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.model.po.CustomerInfo;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {

    CustomerInfo getCustomerInfoById(Long id);
}
