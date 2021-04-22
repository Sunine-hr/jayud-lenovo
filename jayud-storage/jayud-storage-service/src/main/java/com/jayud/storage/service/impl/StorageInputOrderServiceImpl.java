package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.model.bo.StorageInputOrderForm;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.mapper.StorageInputOrderMapper;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.service.IStorageInputOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Override
    public String createOrder(StorageInputOrderForm storageInputOrderForm) {
        StorageInputOrder storageInputOrder = ConvertUtil.convert(storageInputOrderForm, StorageInputOrder.class);
        if(storageInputOrder.getId() == null){
            storageInputOrder.setCreateTime(LocalDateTime.now());
            storageInputOrder.setCreateUser(UserOperator.getToken());
            storageInputOrder.setStatus("CCI_0");
            this.save(storageInputOrder);
        }else{
            storageInputOrder.setId(storageInputOrderForm.getId());
            storageInputOrder.setCreateTime(LocalDateTime.now());
            storageInputOrder.setCreateUser(UserOperator.getToken());
            storageInputOrder.setStatus("CCI_0");
            this.updateById(storageInputOrder);
        }
        String orderNo = storageInputOrder.getOrderNo();
        if(CollectionUtils.isNotEmpty(storageInputOrderForm.getGoodsFormList())){
            List<WarehouseGoodsForm> goodsFormList = storageInputOrderForm.getGoodsFormList();
            List<WarehouseGoods> warehouseGoods = new ArrayList<>();
            for (WarehouseGoodsForm warehouseGood : goodsFormList) {
                warehouseGood.setOrderId(storageInputOrder.getId());
                warehouseGood.setOrderNo(storageInputOrder.getOrderNo());
                warehouseGood.setCreateTime(LocalDateTime.now());
                warehouseGood.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                warehouseGood.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                warehouseGoods.add(ConvertUtil.convert(warehouseGood,WarehouseGoods.class));
            }
            warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
        }
        return orderNo;
    }
}
