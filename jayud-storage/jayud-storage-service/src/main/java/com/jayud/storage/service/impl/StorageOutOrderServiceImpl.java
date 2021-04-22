package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.model.bo.StorageOutOrderForm;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.mapper.StorageOutOrderMapper;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.service.IStorageOutOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Override
    public String createOrder(StorageOutOrderForm storageOutOrderForm) {
        StorageOutOrder storageOutOrder = ConvertUtil.convert(storageOutOrderForm, StorageOutOrder.class);
        if(storageOutOrder.getId() == null){
            storageOutOrder.setCreateTime(LocalDateTime.now());
            storageOutOrder.setCreateUser(UserOperator.getToken());
            storageOutOrder.setStatus("CCI_0");
            this.save(storageOutOrder);
        }else{
            storageOutOrder.setId(storageOutOrderForm.getId());
            storageOutOrder.setCreateTime(LocalDateTime.now());
            storageOutOrder.setCreateUser(UserOperator.getToken());
            storageOutOrder.setStatus("CCI_0");
            this.updateById(storageOutOrder);
        }
        String orderNo = storageOutOrder.getOrderNo();
        if(CollectionUtils.isNotEmpty(storageOutOrderForm.getGoodsFormList())){
            List<WarehouseGoodsForm> goodsFormList = storageOutOrderForm.getGoodsFormList();
            List<WarehouseGoods> warehouseGoods = new ArrayList<>();
            for (WarehouseGoodsForm warehouseGood : goodsFormList) {
                warehouseGood.setOrderId(storageOutOrder.getId());
                warehouseGood.setOrderNo(storageOutOrder.getOrderNo());
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
