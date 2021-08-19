package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.CustomerAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CustomerAddrVO;
import com.jayud.oms.model.vo.CustomerAddressVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户地址 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-08-18
 */
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {

    IPage<CustomerAddrVO> findCustomerAddressByPage(@Param("page") Page<CustomerAddressVO> page,
                                                    @Param("form") QueryCustomerAddressForm form);
}
