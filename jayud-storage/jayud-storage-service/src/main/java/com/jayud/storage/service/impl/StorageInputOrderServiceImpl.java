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
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStorageInputOrderDetailsService;
import com.jayud.storage.service.IStorageInputOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
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
    private RedisUtils redisUtils;

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

    @Override
    public void warehouseReceipt(StorageInProcessOptForm form) {
        form.setOperatorUser(form.getOperatorUser());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        StorageInputOrder storageInputOrder = new StorageInputOrder();
        storageInputOrder.setOrderTaker(form.getOperatorUser());
        storageInputOrder.setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
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
        form.setOperatorUser(form.getOperatorUser());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        StorageInputOrderDetails storageInputOrderDetails = ConvertUtil.convert(form, StorageInputOrderDetails.class);
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
            this.storageProcessOptRecord(form);
            return true;
        }
        if(form.getCmd().equals("end")){
            StorageInputOrder storageInputOrder = new StorageInputOrder();
            storageInputOrder.setId(form.getOrderId());
            storageInputOrder.setIsSubmit(1);
            storageInputOrder.setStatus(form.getStatus());
            this.baseMapper.updateById(storageInputOrder);
            this.storageProcessOptRecord(form);
            return true;
        }
        return false;
    }

    //判断订单完成状态
    private void finishStorageOrderOpt(StorageInputOrder storageInputOrder) {
        if (OrderStatusEnum.CCI_3.getCode().equals(storageInputOrder.getStatus())) {
            //查询海运订单信息
            StorageInputOrder storageInputOrder1 = new StorageInputOrder();
            storageInputOrder1.setId(storageInputOrder.getId());
            storageInputOrder1.setProcessStatus(1);
            this.updateById(storageInputOrder1);
        }

    }
}
