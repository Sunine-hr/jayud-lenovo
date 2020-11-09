package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.CustomerGoods;
import com.jayud.mall.mapper.CustomerGoodsMapper;
import com.jayud.mall.service.ICustomerGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户商品表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomerGoodsServiceImpl extends ServiceImpl<CustomerGoodsMapper, CustomerGoods> implements ICustomerGoodsService {

}
