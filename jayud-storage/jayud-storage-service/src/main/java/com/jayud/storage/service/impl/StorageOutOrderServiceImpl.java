package com.jayud.storage.service.impl;

import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.mapper.StorageOutOrderMapper;
import com.jayud.storage.service.IStorageOutOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出库订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Service
public class StorageOutOrderServiceImpl extends ServiceImpl<StorageOutOrderMapper, StorageOutOrder> implements IStorageOutOrderService {

}
