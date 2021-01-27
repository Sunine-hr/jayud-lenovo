package com.jayud.tms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.bo.QueryDeliveryAddressForm;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.vo.DeliveryAddressVO;

import java.util.List;

/**
 * <p>
 * 提货地址基础数据表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
public interface IDeliveryAddressService extends IService<DeliveryAddress> {

    /**
     * 查询提货/送货地址
     * @param form
     * @return
     */
    List<DeliveryAddressVO> findDeliveryAddress(QueryDeliveryAddressForm form);

    /**
     * 根据手机号查询联系人信息
     * @param phone
     * @return
     */
    List<DeliveryAddress> getContactInfoByPhone(String phone);

    /**
     * 获取最新的联系人信息
     */
    List<DeliveryAddress> getLastContactInfo();
}
