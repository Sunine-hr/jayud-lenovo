package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.CustomerAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CustomerAddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户地址 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Mapper
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {

    IPage<CustomerAddressVO> findCustomerAddressByPage(Page page, @Param("form") QueryCustomerAddressForm form);
}
