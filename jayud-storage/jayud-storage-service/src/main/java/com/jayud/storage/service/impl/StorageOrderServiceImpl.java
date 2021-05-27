package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryOrderStorageForm;
import com.jayud.storage.model.po.StorageOrder;
import com.jayud.storage.mapper.StorageOrderMapper;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.model.vo.StorageOrderVO;
import com.jayud.storage.service.IStorageOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
@Service
public class StorageOrderServiceImpl extends ServiceImpl<StorageOrderMapper, StorageOrder> implements IStorageOrderService {

    @Override
    public IPage<StorageOrderVO> findByPage(QueryOrderStorageForm form) {
        Page<StockVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<StorageOrderVO> pageInfo = this.baseMapper.findByPage(page,form);
        return pageInfo;
    }
}
