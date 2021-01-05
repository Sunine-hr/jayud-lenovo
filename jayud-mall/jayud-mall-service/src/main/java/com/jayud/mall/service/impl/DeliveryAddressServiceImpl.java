package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.CustomerUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.DeliveryAddressMapper;
import com.jayud.mall.model.bo.DeliveryAddressForm;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.po.DeliveryAddress;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.service.IDeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 提货、收货地址基础数据表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Service
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements IDeliveryAddressService {

    @Autowired
    DeliveryAddressMapper deliveryAddressMapper;
    @Autowired
    BaseService baseService;

    @Override
    public IPage<DeliveryAddressVO> findDeliveryAddressByPage(QueryDeliveryAddressForm form) {
        //定义分页参数
        Page<DeliveryAddressVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<DeliveryAddressVO> pageInfo = deliveryAddressMapper.findDeliveryAddressByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult<DeliveryAddressVO> saveDeliveryAddress(DeliveryAddressForm form) {
        DeliveryAddress deliveryAddress = ConvertUtil.convert(form, DeliveryAddress.class);
        Integer id = deliveryAddress.getId();
        if(id == null){
            //新增
            CustomerUser customerUser = baseService.getCustomerUser();
            Integer customerId = customerUser.getId();
            deliveryAddress.setStatus(1);//启用状态0-禁用，1-启用
            deliveryAddress.setCustomerId(customerId);
        }
        this.saveOrUpdate(deliveryAddress);
        DeliveryAddressVO deliveryAddressVO = ConvertUtil.convert(deliveryAddress, DeliveryAddressVO.class);
        return CommonResult.success(deliveryAddressVO);
    }
}
