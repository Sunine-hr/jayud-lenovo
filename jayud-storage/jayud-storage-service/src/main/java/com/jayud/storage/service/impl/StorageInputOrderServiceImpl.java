package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.bo.StorageInputOrderForm;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.mapper.StorageInputOrderMapper;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IStorageInputOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Autowired
    private OauthClient oauthClient;

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

    @Override
    public StorageInputOrder getStorageInOrderByMainOrderNO(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no",orderNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public StorageInputOrderVO getStorageInputOrderVOById(Long id) {
        StorageInputOrder storageInputOrder = this.baseMapper.selectById(id);
        StorageInputOrderVO storageInputOrderVO = ConvertUtil.convert(storageInputOrder, StorageInputOrderVO.class);
        //获取商品信息
        List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList(storageInputOrder.getId(),storageInputOrder.getOrderNo());
        storageInputOrderVO.setGoodsFormList(warehouseGoods);
        return storageInputOrderVO;
    }

    @Override
    public IPage<StorageInputOrderFormVO> findByPage(QueryStorageOrderForm form) {
        if(form.getProcessStatus() !=null ){
            form.setProcessStatusList(Collections.singletonList(form.getProcessStatus()));
        }else{
            if (StringUtils.isEmpty(form.getStatus())) { //订单列表
                form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                        , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
            } else {
                form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
            }

        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<StorageInputOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form,legalIds);
    }
}
