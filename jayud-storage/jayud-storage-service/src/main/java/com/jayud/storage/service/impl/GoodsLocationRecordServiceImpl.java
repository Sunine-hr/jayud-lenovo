package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.WarehousePickingForm;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.mapper.GoodsLocationRecordMapper;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.vo.GoodsLocationRecordFormVO;
import com.jayud.storage.model.vo.InGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.InGoodsOperationRecordNumberVO;
import com.jayud.storage.model.vo.StockLocationNumberVO;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IWarehouseGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品对应库位表（记录入库的商品所在库位以及数量） 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class GoodsLocationRecordServiceImpl extends ServiceImpl<GoodsLocationRecordMapper, GoodsLocationRecord> implements IGoodsLocationRecordService {

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private OmsClient omsClient;

    //入库
    @Override
    public List<GoodsLocationRecord> getGoodsLocationRecordByGoodId(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inGood_id",id);
        queryWrapper.eq("type",1);
        queryWrapper.orderByAsc("create_time");
        return this.baseMapper.selectList(queryWrapper);
    }

    //入库
    @Override
    public List<GoodsLocationRecord> getListByGoodId( String warehousingBatchNo, String sku) {
        return this.baseMapper.getListByGoodId(warehousingBatchNo, sku);
    }

    //入库
    @Override
    public GoodsLocationRecord getGoodsLocationRecordBySkuAndKuCode(String kuCode, String warehousingBatchNo, String sku) {
        InGoodsOperationRecord inGoodsOperationRecord = inGoodsOperationRecordService.getListByWarehousingBatchNoAndSku(warehousingBatchNo,sku);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inGood_id",inGoodsOperationRecord.getId());
        queryWrapper.eq("ku_code",kuCode);
        queryWrapper.eq("type",1);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public StockLocationNumberVO getListBySkuAndLocationCode(String sku, String locationCode,Long customerId) {
        //获取该商品该库位的所有入库数量
        List<InGoodsOperationRecordFormVO> inGoodsOperationRecordFormVOS = inGoodsOperationRecordService.getListBySkuAndLocationCode(sku,locationCode,customerId);
        StockLocationNumberVO stockLocationNumberVO = new StockLocationNumberVO();
        Integer number = 0;
        for (InGoodsOperationRecordFormVO inGoodsOperationRecordFormVO : inGoodsOperationRecordFormVOS) {
            number = number + inGoodsOperationRecordFormVO.getNowNumber();
        }
        //获取锁定库存
        Integer result = warehouseGoodsService.getCount(sku, locationCode, customerId);
        stockLocationNumberVO.setGoodName(inGoodsOperationRecordFormVOS.get(0).getName());
        stockLocationNumberVO.setCustomerId(customerId);
        stockLocationNumberVO.setCustomerName(omsClient.getCustomerNameById(customerId).getData());
        stockLocationNumberVO.setSku(sku);
        stockLocationNumberVO.setAvailableStock(number);
        stockLocationNumberVO.setLockStock(result);
        return stockLocationNumberVO;
    }

    @Override
    public List<GoodsLocationRecordFormVO> getOutGoodsLocationRecordByGoodId(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inGood_id",id);
        queryWrapper.eq("type",2);
        List<GoodsLocationRecord> list = this.baseMapper.selectList(queryWrapper);
        List<GoodsLocationRecordFormVO> goodsLocationRecordFormVOS = ConvertUtil.convertList(list, GoodsLocationRecordFormVO.class);
        return goodsLocationRecordFormVOS;
    }

    @Override
    public GoodsLocationRecord getInListByKuCodeAndInGoodId(String kuCode, Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inGood_id",id);
        queryWrapper.eq("ku_code",kuCode);
        queryWrapper.eq("type",1);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<GoodsLocationRecordFormVO> getOutGoodsLocationRecordByGoodIdAndPicked(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inGood_id",id);
        queryWrapper.eq("type",2);
        queryWrapper.eq("is_picked_goods",1);
        List<GoodsLocationRecord> list = this.baseMapper.selectList(queryWrapper);
        List<GoodsLocationRecordFormVO> goodsLocationRecordFormVOS = ConvertUtil.convertList(list, GoodsLocationRecordFormVO.class);
        return goodsLocationRecordFormVOS;
    }

    @Override
    public GoodsLocationRecord getOutListByKuCodeAndInGoodId(Long id, String kuCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("inGood_id",id);
        queryWrapper.eq("ku_code",kuCode);
        queryWrapper.eq("type",2);
        return this.baseMapper.selectOne(queryWrapper);
    }


}
