package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.model.bo.StorageFastOrderForm;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageFastOrder;
import com.jayud.storage.mapper.StorageFastOrderMapper;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.model.vo.InGoodsOperationRecordVO;
import com.jayud.storage.model.vo.StorageFastOrderVO;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IStorageFastOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 快进快出订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
@Service
public class StorageFastOrderServiceImpl extends ServiceImpl<StorageFastOrderMapper, StorageFastOrder> implements IStorageFastOrderService {

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private FileClient fileClient;

    @Override
    public String createOrder(StorageFastOrderForm storageFastOrderForm) {
        //无论驳回还是草稿在提交，都先删除原来的商品信息
        if(storageFastOrderForm.getId()!=null){
            warehouseGoodsService.deleteWarehouseGoodsFormsByOrderId(storageFastOrderForm.getId());
        }
        //创建
        StorageFastOrder storageFastOrder = ConvertUtil.convert(storageFastOrderForm, StorageFastOrder.class);
        if(storageFastOrder.getId() == null){
            storageFastOrder.setCreateTime(LocalDateTime.now());
            storageFastOrder.setCreateUser(UserOperator.getToken());
            storageFastOrder.setStatus("CCF_0");
            this.save(storageFastOrder);
        }else{
            storageFastOrder.setId(storageFastOrderForm.getId());
            storageFastOrder.setCreateTime(LocalDateTime.now());
            storageFastOrder.setCreateUser(UserOperator.getToken());
            storageFastOrder.setStatus("CCF_0");
            this.updateById(storageFastOrder);
        }
        String orderNo = storageFastOrder.getOrderNo();
        if(storageFastOrder.getIsWarehouse().equals(0)){
            if(CollectionUtils.isNotEmpty(storageFastOrderForm.getFastGoodsFormList())){
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getFastGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(3);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
        }else{
            if(CollectionUtils.isNotEmpty(storageFastOrderForm.getInGoodsFormList())){
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getInGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(4);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
            if(CollectionUtils.isNotEmpty(storageFastOrderForm.getOutGoodsFormList())){
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getOutGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(5);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
        }

        return orderNo;
    }

    @Override
    public StorageFastOrder getStorageFastOrderByMainOrderNO(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no",orderNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public StorageFastOrderVO getStorageFastOrderVOById(Long id) {
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        StorageFastOrder storageFastOrder = this.baseMapper.selectById(id);
        StorageFastOrderVO storageFastOrderVO = ConvertUtil.convert(storageFastOrder, StorageFastOrderVO.class);
        //获取商品信息
        if(storageFastOrder.getIsWarehouse().equals(0)){
            List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList(storageFastOrderVO.getId(),storageFastOrderVO.getOrderNo(),2);
            if(CollectionUtils.isEmpty(warehouseGoods)){
                warehouseGoods.add(new WarehouseGoodsVO());
            }
            for (WarehouseGoodsVO warehouseGood : warehouseGoods) {
                warehouseGood.setTakeFiles(StringUtils.getFileViews(warehouseGood.getFilePath(),warehouseGood.getFileName(),prePath));
            }
            storageFastOrderVO.setFastGoodsFormList(warehouseGoods);

        }else{
            List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList(storageFastOrderVO.getId(),storageFastOrderVO.getOrderNo(),3);
            if(CollectionUtils.isEmpty(warehouseGoods)){
                warehouseGoods.add(new WarehouseGoodsVO());
            }
            //获取附件信息
            for (WarehouseGoodsVO warehouseGood : warehouseGoods) {
                warehouseGood.setTakeFiles(StringUtils.getFileViews(warehouseGood.getFilePath(),warehouseGood.getFileName(),prePath));
            }
            storageFastOrderVO.setInGoodsFormList(warehouseGoods);

            List<WarehouseGoodsVO> warehouseGoodsVOS = warehouseGoodsService.getList(storageFastOrderVO.getId(),storageFastOrderVO.getOrderNo(),4);
            if(CollectionUtils.isEmpty(warehouseGoodsVOS)){
                warehouseGoodsVOS.add(new WarehouseGoodsVO());
            }
            //获取附件信息
            for (WarehouseGoodsVO warehouseGood : warehouseGoodsVOS) {
                warehouseGood.setTakeFiles(StringUtils.getFileViews(warehouseGood.getFilePath(),warehouseGood.getFileName(),prePath));
            }
            storageFastOrderVO.setOutGoodsFormList(warehouseGoodsVOS);
        }
        return storageFastOrderVO;
    }
}
