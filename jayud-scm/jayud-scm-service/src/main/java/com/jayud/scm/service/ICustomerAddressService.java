package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerAddressForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerAddressVO;

import java.util.List;

/**
 * <p>
 * 客户常用地址表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerAddressService extends IService<CustomerAddress> {

    IPage<CustomerAddressVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerAddress(AddCustomerAddressForm form);

    boolean modifyDefaultValues(AddCustomerAddressForm form);

    CustomerAddressVO getCustomerAddressById(Integer id);

    List<CustomerAddressVO> getCustomerAddressByCustomerIdAndSType(Integer customerId, String sType);

    List<CustomerAddressVO> getCustomerAddressByAddressAndSType(String address, String sType);
}
