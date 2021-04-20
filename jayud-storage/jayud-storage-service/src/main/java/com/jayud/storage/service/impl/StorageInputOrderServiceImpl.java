package com.jayud.storage.service.impl;

import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.mapper.StorageInputOrderMapper;
import com.jayud.storage.service.IStorageInputOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 入库订单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class StorageInputOrderServiceImpl extends ServiceImpl<StorageInputOrderMapper, StorageInputOrder> implements IStorageInputOrderService {

}
