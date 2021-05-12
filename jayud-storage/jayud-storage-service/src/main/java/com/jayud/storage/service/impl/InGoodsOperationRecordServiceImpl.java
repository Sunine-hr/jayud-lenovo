package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.mapper.InGoodsOperationRecordMapper;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 入库商品操作记录表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class InGoodsOperationRecordServiceImpl extends ServiceImpl<InGoodsOperationRecordMapper, InGoodsOperationRecord> implements IInGoodsOperationRecordService {

    @Override
    public List<InGoodsOperationRecord> getList(Long id, String orderNo,String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("order_id",id);
        queryWrapper.eq("name",name);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<InGoodsOperationRecord> getListByWarehousingBatchNo(String warehousingBatchNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("warehousing_batch_no",warehousingBatchNo);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<InGoodsOperationRecord> getListByOrderId(Long id, String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("order_id",id);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<InGoodsOperationRecord> getListByOrderId(Long id) {
        return this.baseMapper.getListByOrderId(id);
    }
}
