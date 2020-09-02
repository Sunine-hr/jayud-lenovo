package com.jayud.oms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.CustomerInfoVO;

/**
 * 客户信息
 */
public interface ICustomerInfoService extends IService<CustomerInfo> {

    /**
     * 根据id获取客户信息
     * @param id
     * @return
     */
    CustomerInfoVO getCustomerInfoById(Long id);

}
