package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.po.DeliveryAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提货地址基础数据表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Mapper
@Component
public interface DeliveryAddressMapper extends BaseMapper<DeliveryAddress> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<DeliveryAddressVO> findDeliveryAddressByPage(Page<DeliveryAddressVO> page,@Param("form") QueryDeliveryAddressForm form);

    /**
     * 根据id，查询地址
     * @param id
     * @return
     */
    DeliveryAddressVO findDeliveryAddressById(@Param("id") Integer id);
}
