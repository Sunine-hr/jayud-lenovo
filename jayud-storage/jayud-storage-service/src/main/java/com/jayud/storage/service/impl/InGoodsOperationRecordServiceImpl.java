package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.mapper.InGoodsOperationRecordMapper;
import com.jayud.storage.model.vo.InGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.InGoodsOperationRecordNumberVO;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Override
    public List<InGoodsOperationRecord> getList(Long id, String orderNo,String sku) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper.eq("order_id",id);
        queryWrapper.eq("sku",sku);
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

    /**
     * 查询该sku商品的所有入库批次
     * @param sku
     * @return
     */
    @Override
    public List<InGoodsOperationRecord> getListBySku(String sku) {
        return this.baseMapper.getListBySku(sku);
    }

    @Override
    public InGoodsOperationRecord getListByWarehousingBatchNoAndSku(String warehousingBatchNo, String sku) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("warehousing_batch_no",warehousingBatchNo);
        queryWrapper.eq("sku",sku);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<InGoodsOperationRecordFormVO> getListBySkuAndLocationCode(String sku, String locationCode,Long customerId) {
        return this.baseMapper.getListBySkuAndLocationCode(sku,locationCode,customerId);
    }

    @Override
    public List<String> getWarehousingBatch(Long id) {
        return this.baseMapper.getWarehousingBatch(id);
    }

    @Override
    public List<InGoodsOperationRecord> getList1() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        List<InGoodsOperationRecord> list = this.baseMapper.selectList(queryWrapper);
        List<InGoodsOperationRecord> list1 = new ArrayList<>();
        for (InGoodsOperationRecord inGoodsOperationRecord : list) {
            List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(inGoodsOperationRecord.getId());
            Integer number = 0;
            for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecordByGoodId) {
                number = number + goodsLocationRecord.getUnDeliveredQuantity();
            }
            if(number>0){
                list1.add(inGoodsOperationRecord);
            }
        }
        return list1;
    }


}
