package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
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
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.mapper.StorageOutOrderMapper;
import com.jayud.storage.model.po.WarehouseGoods;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.StorageOutOrderFormVO;
import com.jayud.storage.model.vo.StorageOutOrderVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IStorageOutOrderService;
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

    @Autowired
    private OmsClient omsClient;

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
    public IPage<StorageOutOrderFormVO> findByPage(QueryStorageOrderForm form) {
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

        Page<StorageOutOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form,legalIds);
    }

    @Override
    public void orderReceiving(StorageOutOrder storageOutOrder, AuditInfoForm auditInfoForm, StorageOutCargoRejected storageOutCargoRejected) {
        StorageOutOrder tmp = new StorageOutOrder();
        tmp.setId(storageOutOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);
        omsClient.doMainOrderRejectionSignOpt(storageOutOrder.getMainOrderNo(),
                storageOutOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");

        omsClient.saveAuditInfo(auditInfoForm);
        this.updateById(tmp);
    }

    @Override
    public void warehouseReceipt(StorageOutProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        StorageOutOrder storageOutOrder = new StorageOutOrder();
        storageOutOrder.setOrderTaker(form.getOperatorUser());
        storageOutOrder.setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        storageOutOrder.setId(form.getOrderId());
        storageOutOrder.setUpdateUser(UserOperator.getToken());
        storageOutOrder.setUpdateTime(LocalDateTime.now());
        storageOutOrder.setStatus(form.getStatus());

        boolean b = this.updateById(storageOutOrder);
        if(!b){
            log.warn("修改状态失败");
        }
        //节点操作记录
        this.storageProcessOptRecord(form);

        //完成订单状态
        finishStorageOrderOpt(storageOutOrder);
    }

    /**
     * 入库流程操作记录
     */
    @Override
    public void storageProcessOptRecord(StorageOutProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.STORAGE_OUT_ORDER);
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
    public boolean printPickingList(StorageOutProcessOptForm form) {
        return false;
    }

    @Override
    public boolean warehousePicking(StorageOutProcessOptForm form) {
        return false;
    }

    @Override
    public void abnormalOutOfWarehouse(StorageOutProcessOptForm form) {

        form.setOperatorUser(UserOperator.getToken());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        StorageOutOrder storageOutOrder = new StorageOutOrder();
        storageOutOrder.setId(form.getOrderId());
        storageOutOrder.setUpdateUser(UserOperator.getToken());
        storageOutOrder.setUpdateTime(LocalDateTime.now());
        storageOutOrder.setStatus(OrderStatusEnum.CCE_3.getCode());

        boolean b = this.updateById(storageOutOrder);
        if(!b){
            log.warn("修改状态失败");
        }
        //节点操作记录
        this.storageProcessOptRecord(form);

        //完成订单状态
        finishStorageOrderOpt(storageOutOrder);
    }

    @Override
    public void confirmDelivery(StorageOutProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        form.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN).toString());
        StorageOutOrder storageOutOrder = new StorageOutOrder();
        storageOutOrder.setId(form.getOrderId());
        storageOutOrder.setUpdateUser(UserOperator.getToken());
        storageOutOrder.setUpdateTime(LocalDateTime.now());
        storageOutOrder.setStatus(form.getStatus());

        boolean b = this.updateById(storageOutOrder);
        if(!b){
            log.warn("修改状态失败");
        }
        if(form.getCmd().equals("success")){
            //节点操作记录
            this.storageProcessOptRecord(form);

            //完成订单状态
            finishStorageOrderOpt(storageOutOrder);
        }
        if(form.getCmd().equals("fail")){
            form.setDescription("确认出库，审核不通过");
            if(!b){
                log.warn("修改状态失败");
            }
            //节点操作记录
            this.storageProcessOptRecord(form);
        }
    }

    //判断订单完成状态
    private void finishStorageOrderOpt(StorageOutOrder storageOutOrder) {
        if (OrderStatusEnum.CCE_4.getCode().equals(storageOutOrder.getStatus())) {
            //查询入库订单信息
            StorageOutOrder storageOutOrder1 = new StorageOutOrder();
            storageOutOrder1.setId(storageOutOrder.getId());
            storageOutOrder1.setProcessStatus(1);
            this.updateById(storageOutOrder1);
        }

    }
}
