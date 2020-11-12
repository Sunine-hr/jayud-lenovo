package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.DeliveryAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CustomerAddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 提货地址基础数据表 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-12
 */
@Mapper
public interface DeliveryAddressMapper extends BaseMapper<DeliveryAddress> {
    IPage<CustomerAddressVO> findCustomerAddressByPage(Page page, @Param("form") QueryCustomerAddressForm form);
}
