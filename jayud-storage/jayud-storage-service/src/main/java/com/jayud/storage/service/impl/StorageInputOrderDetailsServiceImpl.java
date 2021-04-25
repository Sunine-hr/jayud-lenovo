package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.storage.model.po.StorageInputOrderDetails;
import com.jayud.storage.mapper.StorageInputOrderDetailsMapper;
import com.jayud.storage.service.IStorageInputOrderDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 入库订单详情表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class StorageInputOrderDetailsServiceImpl extends ServiceImpl<StorageInputOrderDetailsMapper, StorageInputOrderDetails> implements IStorageInputOrderDetailsService {

    @Override
    public StorageInputOrderDetails getStorageInputOrderDetails(Long orderId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",orderId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
