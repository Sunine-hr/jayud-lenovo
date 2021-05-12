package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.mapper.StorageInputOrderMapper;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.StorageInputOrderWarehouseingVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.httpclient.HttpStatus;
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

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IStorageInputOrderDetailsService storageInputOrderDetailsService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IStockService stockService;

    @Override
    public String createOrder(StorageInputOrderForm storageInputOrderForm) {

        //无论驳回还是草稿在提交，都先删除原来的商品信息
        if(storageInputOrderForm.getId()!=null){
            warehouseGoodsService.deleteWarehouseGoodsFormsByOrderId(storageInputOrderForm.getId());
        }
        //创建
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
                WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                convert.setOrderId(storageInputOrder.getId());
                convert.setOrderNo(storageInputOrder.getOrderNo());
                convert.setType(1);
                convert.setCreateTime(LocalDateTime.now());
                convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                warehouseGoods.add(convert);
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
        if(CollectionUtils.isEmpty(warehouseGoods)){
            warehouseGoods.add(new WarehouseGoodsVO());
            storageInputOrderVO.setTotalNumber("0板0件0pcs");
            storageInputOrderVO.setTotalWeight("0KG");
        }else{
            double totalWeight = 0.0;
            Integer borderNumber = 0;
            Integer number = 0;
            Integer pcs = 0;
            for (WarehouseGoodsVO warehouseGood : warehouseGoods) {
                if(warehouseGood.getWeight()!=null){
                    totalWeight = totalWeight + warehouseGood.getWeight();
                }
                if(warehouseGood.getBoardNumber()!=null){
                    borderNumber = borderNumber + warehouseGood.getBoardNumber();
                }
                if(warehouseGood.getNumber()!=null){
                    number = number + warehouseGood.getNumber();
                }
                if(warehouseGood.getPcs()!=null){
                    pcs = pcs + warehouseGood.getPcs();
                }
            }
            storageInputOrderVO.setTotalNumber(borderNumber+"板"+number+"件"+pcs+"pcs");
            storageInputOrderVO.setTotalWeight(totalWeight+"KG");
        }
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

    @Override
    public IPage<StorageInputOrderWarehouseingVO> findWarehousingByPage(QueryStorageOrderForm form) {
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

        Page<StorageInputOrderWarehouseingVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findWarehousingByPage(page, form,legalIds);
    }

    @Override
    public boolean warehousingEntry(StorageInProcessOptForm form) {
        List<InGoodsOperationRecordForm> inGoodsOperationRecords = form.getInGoodsOperationRecords();
        for (InGoodsOperationRecordForm inGoodsOperationRecord : inGoodsOperationRecords) {
            List<GoodsLocationRecordForm> goodsLocationRecordForms = inGoodsOperationRecord.getGoodsLocationRecordForms();
            for (GoodsLocationRecordForm goodsLocationRecordForm : goodsLocationRecordForms) {
                if(goodsLocationRecordForm.getKuId()!=null && goodsLocationRecordForm.getNumber()!=null){
                    GoodsLocationRecord goodsLocationRecord = ConvertUtil.convert(goodsLocationRecordForm, GoodsLocationRecord.class);
                    goodsLocationRecord.setInGoodId(inGoodsOperationRecord.getId());
                    goodsLocationRecord.setCreateUser(UserOperator.getToken());
                    goodsLocationRecord.setCreateTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                    goodsLocationRecord.setType(1);
                    boolean b = goodsLocationRecordService.saveOrUpdate(goodsLocationRecord);
                    if(!b){
                        return false;
                    }
                    Stock stock = new Stock();
                    stock.setGoodName(inGoodsOperationRecord.getName());
                    stock.setSku(inGoodsOperationRecord.getSku());
                    stock.setSpecificationModel(inGoodsOperationRecord.getSpecificationModel());
                    stock.setLockStock(goodsLocationRecord.getNumber());
                    boolean result = stockService.saveStock(stock);
                    if(!result){
                        return false;
                    }
                }

            }
            InGoodsOperationRecord inGoodsOperationRecord1 = ConvertUtil.convert(inGoodsOperationRecord, InGoodsOperationRecord.class);
            inGoodsOperationRecord1.setIsWarehousing(1);
            boolean b = inGoodsOperationRecordService.saveOrUpdate(inGoodsOperationRecord1);
            if(!b){
                return false;
            }
        }
        //获取该订单所有入仓信息
        boolean flag = true;
        List<InGoodsOperationRecord> listByOrderId = inGoodsOperationRecordService.getListByOrderId(form.getId(), form.getOrderNo());
        for (InGoodsOperationRecord inGoodsOperationRecord : listByOrderId) {
            if(!inGoodsOperationRecord.getIsWarehousing().equals(1)){
                flag = false;
            }
        }
        if(flag){
            StorageInputOrder storageInputOrder = new StorageInputOrder();
            storageInputOrder.setId(form.getId());
            storageInputOrder.setStatus(form.getStatus());
            this.baseMapper.updateById(storageInputOrder);
            this.finishStorageOrderOpt(storageInputOrder);
            this.storageProcessOptRecord(form);
            return true;
        }
        if(!flag){
            StorageInputOrder storageInputOrder = new StorageInputOrder();
            storageInputOrder.setId(form.getId());
            form.setStatusName("该批次入库成功");
            form.setStatus("CCI_2");
            this.baseMapper.updateById(storageInputOrder);
            this.finishStorageOrderOpt(storageInputOrder);
            this.storageProcessOptRecord(form);
            return true;
        }
        return false;
    }

    @Override
    public void orderReceiving(StorageInputOrder storageInputOrder, AuditInfoForm auditInfoForm, StorageInCargoRejected storageInCargoRejected) {
        StorageInputOrder tmp = new StorageInputOrder();
        tmp.setId(storageInputOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);
        omsClient.doMainOrderRejectionSignOpt(storageInputOrder.getMainOrderNo(),
                storageInputOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");

        omsClient.saveAuditInfo(auditInfoForm);
        this.updateById(tmp);
    }

    @Override
    public void warehouseReceipt(StorageInProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        StorageInputOrder storageInputOrder = new StorageInputOrder();
        storageInputOrder.setOrderTaker(form.getOperatorUser());
        storageInputOrder.setReceivingOrdersDate(form.getOperatorTime());
        storageInputOrder.setId(form.getOrderId());
        storageInputOrder.setUpdateUser(UserOperator.getToken());
        storageInputOrder.setUpdateTime(LocalDateTime.now());
        storageInputOrder.setStatus(form.getStatus());

        boolean b = this.updateById(storageInputOrder);
        if(!b){
            log.warn("修改状态失败");
        }
        //节点操作记录
        this.storageProcessOptRecord(form);

        //完成订单状态
        finishStorageOrderOpt(storageInputOrder);
    }

    /**
     * 入库流程操作记录
     */
    @Override
    public void storageProcessOptRecord(StorageInProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.STORAGE_INPUT_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(UserOperator.getToken());
        auditInfoForm.setFileViews(form.getFileViewList());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(form.getStatusName());

        //文件拼接
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setBusinessType(BusinessTypeEnum.TC.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }

    }

    @Override
    public boolean confirmEntry(StorageInProcessOptForm form) {
        redisUtils.delete(form.getOrderNo());
        form.setOperatorUser(UserOperator.getToken());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());

        StorageInputOrderDetails storageInputOrderDetails = ConvertUtil.convert(form, StorageInputOrderDetails.class);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer s = new StringBuffer();
        for (Long aLong : form.getOperationId()) {
            stringBuffer.append(aLong).append(",");
        }
        for (Long aLong : form.getCardtypeId()) {
            s.append(aLong).append(",");
        }
        storageInputOrderDetails.setCardTypeId(s.substring(0,s.length()-1).toString());
        storageInputOrderDetails.setOperationId(stringBuffer.substring(0,s.length()-1).toString());
        boolean insert = storageInputOrderDetailsService.saveOrUpdate(storageInputOrderDetails);
        if(!insert){
            return false;
        }
        List<WarehouseGoodsForm> warehouseGoodsForms = form.getWarehouseGoodsForms();
        //在添加商品前，先删除原来的商品信息
        warehouseGoodsService.deleteWarehouseGoodsFormsByOrder(form.getOrderId(),form.getOrderNo());

        for (WarehouseGoodsForm warehouseGoodsForm : warehouseGoodsForms) {
            WarehouseGoods warehouseGoods = ConvertUtil.convert(warehouseGoodsForm, WarehouseGoods.class);
            boolean b = warehouseGoodsService.saveOrUpdate(warehouseGoods);
            if(!b){
                return false;
            }
            InGoodsOperationRecord inGoodsOperationRecord = new InGoodsOperationRecord();
            inGoodsOperationRecord.setOrderId(warehouseGoodsForm.getOrderId());
            inGoodsOperationRecord.setOrderNo(warehouseGoodsForm.getOrderNo());
            inGoodsOperationRecord.setWarehousingBatchNo(form.getWarehousingBatchNo());
            inGoodsOperationRecord.setName(warehouseGoodsForm.getName());
            inGoodsOperationRecord.setSku(warehouseGoodsForm.getSku());
            inGoodsOperationRecord.setSpecificationModel(warehouseGoodsForm.getSpecificationModel());
            inGoodsOperationRecord.setBoardNumber(warehouseGoodsForm.getSjBoardNumber());
            inGoodsOperationRecord.setNumber(warehouseGoodsForm.getSjNumber());
            inGoodsOperationRecord.setPcs(warehouseGoodsForm.getSjPcs());
            inGoodsOperationRecord.setWeight(warehouseGoodsForm.getSjWeight());
            inGoodsOperationRecord.setVolume(warehouseGoodsForm.getSjVolume());
            boolean save = inGoodsOperationRecordService.save(inGoodsOperationRecord);
            if(!save){
                return false;
            }
        }
        if(form.getCmd().equals("submit")){

            //订单状况变为确认入仓已提交
            StorageInputOrder storageInputOrder = new StorageInputOrder();
            storageInputOrder.setId(form.getOrderId());
            storageInputOrder.setIsSubmit(1);
            form.setStatusName("确认入仓提交");
            form.setStatus("CCI_1");
            this.baseMapper.updateById(storageInputOrder);
            this.finishStorageOrderOpt(storageInputOrder);
            this.storageProcessOptRecord(form);
            return true;
        }
        if(form.getCmd().equals("end")){
            StorageInputOrder storageInputOrder = new StorageInputOrder();
            storageInputOrder.setId(form.getOrderId());
            storageInputOrder.setIsSubmit(1);
            storageInputOrder.setStatus(form.getStatus());
            this.baseMapper.updateById(storageInputOrder);
            this.finishStorageOrderOpt(storageInputOrder);
            this.storageProcessOptRecord(form);
            return true;
        }
        return false;
    }


    //判断订单完成状态
    private void finishStorageOrderOpt(StorageInputOrder storageInputOrder) {
        if (OrderStatusEnum.CCI_3.getCode().equals(storageInputOrder.getStatus())) {
            //查询入库订单信息
            StorageInputOrder storageInputOrder1 = new StorageInputOrder();
            storageInputOrder1.setId(storageInputOrder.getId());
            storageInputOrder1.setProcessStatus(1);
            this.updateById(storageInputOrder1);
        }

    }
}
