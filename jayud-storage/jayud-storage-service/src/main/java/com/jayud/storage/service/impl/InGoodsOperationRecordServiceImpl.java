package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.mapper.InGoodsOperationRecordMapper;
import com.jayud.storage.model.vo.*;
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
        queryWrapper.eq("is_warehousing",0);
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
        List<InGoodsOperationRecordFormVO> listBySkuAndLocationCode = this.baseMapper.getListBySkuAndLocationCode(sku, locationCode, customerId);

        return listBySkuAndLocationCode;
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

    @Override
    public List<InGoodsOperationRecord> getListByAreaName(String areaName) {
        return this.baseMapper.getListByAreaName(areaName);
    }

    @Override
    public List<QRCodeLocationGoodVO> getListByKuCode(String kuCode) {
        List<QRCodeLocationGoodVO> listByKuCode = this.baseMapper.getListByKuCode(kuCode);
        for  ( int  i  =   0 ; i  <  listByKuCode.size()  -   1 ; i ++ )  {
            for  ( int  j  =  listByKuCode.size()  -   1 ; j  >  i; j -- )  {
                if  (listByKuCode.get(j).getSku().equals(listByKuCode.get(i).getSku()))  {
                    listByKuCode.get(i).setNumber(listByKuCode.get(i).getNumber() + listByKuCode.get(j).getNumber());
                    listByKuCode.remove(j);
                }
            }
        }
        return listByKuCode;
    }

    @Override
    public List<String> getWarehousingBatchNoComBox(String kuCode, String sku) {
        return this.baseMapper.getWarehousingBatchNoComBox(kuCode,sku);
    }

    @Override
    public List<InGoodsOperationRecordVO> getListByWarehousingBatchNoAndOrderNo(String warehousingBatchNo, String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("warehousing_batch_no",warehousingBatchNo);
        queryWrapper.eq("order_no",orderNo);
        List<InGoodsOperationRecord> list = this.baseMapper.selectList(queryWrapper);
        List<InGoodsOperationRecordVO> inGoodsOperationRecordVOS = ConvertUtil.convertList(list, InGoodsOperationRecordVO.class);
        return inGoodsOperationRecordVOS;
    }

    @Override
    public List<OnShelfOrderVO> getListByOrderIdAndTime2(Long id, String orderNo, String startTime, String endTime) {
        return this.baseMapper.getListByOrderIdAndTime2(id,orderNo,startTime,endTime);
    }


}
