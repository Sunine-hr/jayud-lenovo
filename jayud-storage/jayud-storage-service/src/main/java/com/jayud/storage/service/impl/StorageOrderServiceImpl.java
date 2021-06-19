package com.jayud.storage.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderTypeEnum;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryOrderStorageForm;
import com.jayud.storage.model.po.Good;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageOrder;
import com.jayud.storage.mapper.StorageOrderMapper;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.model.vo.StorageOrderVO;
import com.jayud.storage.service.IGoodService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStorageOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
@Service
public class StorageOrderServiceImpl extends ServiceImpl<StorageOrderMapper, StorageOrder> implements IStorageOrderService {

    @Autowired
    private IGoodService goodService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Override
    public IPage<StorageOrderVO> findByPage(QueryOrderStorageForm form) {
        Page<StockVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<StorageOrderVO> pageInfo = this.baseMapper.findByPage(page,form);
        return pageInfo;
    }

    @Override
    public boolean saveStorageOrder(StorageOrder storageOrder) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku",storageOrder.getSku());
        Good one = goodService.getOne(queryWrapper);

        storageOrder.setOrderNo(getOrderNo());
        storageOrder.setCustomerName(omsClient.getCustomerNameById(one.getCustomerId()).getData());
        storageOrder.setCreateTime(LocalDateTime.now());
        storageOrder.setMonth(String.valueOf(LocalDateTime.now().getMonthValue()));
        storageOrder.setStatus(1);
        InGoodsOperationRecord listByWarehousingBatchNoAndSku = inGoodsOperationRecordService.getListByWarehousingBatchNoAndSku(storageOrder.getWarehousingBatchNo(), storageOrder.getSku());
        storageOrder.setStartTime(listByWarehousingBatchNoAndSku.getCreateTime());
        return this.save(storageOrder);
    }

    @Override
    public List<StorageOrderVO> getList(String orderNo, String outOrderNo, String customerName, String month) {
        return this.baseMapper.getList(orderNo,outOrderNo,customerName,month);
    }

    public String getOrderNo() {
        String orderNo = (String) omsClient.getWarehouseNumber("CC").getData();
        return orderNo;
    }
}
