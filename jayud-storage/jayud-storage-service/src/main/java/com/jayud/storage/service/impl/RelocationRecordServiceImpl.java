package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.bo.RelocationRecordForm;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.RelocationRecord;
import com.jayud.storage.mapper.RelocationRecordMapper;
import com.jayud.storage.model.vo.RelocationGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IRelocationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 移库信息表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Service
public class RelocationRecordServiceImpl extends ServiceImpl<RelocationRecordMapper, RelocationRecord> implements IRelocationRecordService {

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Override
    public List<RelocationGoodsOperationRecordFormVO> getListBySkuAndLocationCode(String sku, String locationCode) {
        return this.baseMapper.getListBySkuAndLocationCode(sku,locationCode);
    }

    @Override
    public IPage<RelocationRecordVO> findByPage(QueryRelocationRecordForm form) {
        Page<StockVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<RelocationRecordVO> pageInfo = this.baseMapper.findByPage(page,form);
        return pageInfo;
    }

    @Override
    public List<RelocationRecordVO> getList(String orderNo, String warehouseName, String areaName, String shelvesName, String sku, String startTime, String endTime) {
        return this.baseMapper.getList(orderNo,warehouseName,areaName,shelvesName,sku,startTime,endTime);
    }

    @Override
    public boolean createRelocationOrder(RelocationRecordForm form) {
        RelocationRecord relocationRecord = ConvertUtil.convert(form, RelocationRecord.class);
        relocationRecord.setCreateTime(LocalDateTime.now());
        relocationRecord.setCreateUser(UserOperator.getToken());
        boolean update = this.saveOrUpdate(relocationRecord);
        if(update){
            //移库成功，修改库位信息
            InGoodsOperationRecord listByWarehousingBatchNoAndSku = inGoodsOperationRecordService.getListByWarehousingBatchNoAndSku(form.getWarehousingBatchNo(), form.getSku());
            GoodsLocationRecord inListByKuCodeAndInGoodId = goodsLocationRecordService.getInListByKuCodeAndInGoodId(form.getOldLocationCode(), listByWarehousingBatchNoAndSku.getId());
            inListByKuCodeAndInGoodId.setNumber(inListByKuCodeAndInGoodId.getNumber()-form.getNumber());
            boolean update1 = goodsLocationRecordService.saveOrUpdate(inListByKuCodeAndInGoodId);
            if(!update1){
                log.warn("修改库位商品信息失败");
            }
            GoodsLocationRecord inListByKuCodeAndInGoodId1 = goodsLocationRecordService.getInListByKuCodeAndInGoodId(form.getNewLocationCode(), listByWarehousingBatchNoAndSku.getId());
            if(inListByKuCodeAndInGoodId == null){
                GoodsLocationRecord goodsLocationRecord = new GoodsLocationRecord();
                goodsLocationRecord.setNumber(form.getNumber());
                goodsLocationRecord.setUnDeliveredQuantity(form.getNumber());
                goodsLocationRecord.setKuCode(form.getNewLocationCode());
                goodsLocationRecord.setCreateUser(UserOperator.getToken());
                goodsLocationRecord.setCreateTime(LocalDateTime.now());
                goodsLocationRecord.setType(1);
                goodsLocationRecord.setInGoodId(listByWarehousingBatchNoAndSku.getId());
                boolean update2 = goodsLocationRecordService.saveOrUpdate(goodsLocationRecord);
                if(!update2){
                    log.warn("添加库位商品信息失败");
                }
            }else{
                inListByKuCodeAndInGoodId1.setNumber(inListByKuCodeAndInGoodId1.getNumber() + form.getNumber());
                boolean update3 = goodsLocationRecordService.saveOrUpdate(inListByKuCodeAndInGoodId1);
                if(!update3){
                    log.warn("修改库位商品信息失败");
                }
            }

        }
        return update;
    }
}
