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
import com.jayud.storage.model.bo.StorageOutOrderForm;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.mapper.StorageOutOrderMapper;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageOutOrderVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IStorageOutOrderService;
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

    @Autowired
    private OauthClient oauthClient;

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

    /**
     * 根据主订单获取出库订单数据
     * @param orderNo
     * @return
     */
    @Override
    public StorageOutOrder getStorageOutOrderByMainOrderNO(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no",orderNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据id获取订单数据
     * @param id
     * @return
     */
    @Override
    public StorageOutOrderVO getStorageOutOrderVOById(Long id) {
        StorageOutOrder storageOutOrder = this.baseMapper.selectById(id);
        StorageOutOrderVO storageOutOrderVO = ConvertUtil.convert(storageOutOrder, StorageOutOrderVO.class);
        List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList(storageOutOrder.getId(),storageOutOrder.getOrderNo());
        storageOutOrderVO.setGoodsFormList(warehouseGoods);
        return storageOutOrderVO;
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
