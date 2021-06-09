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
import com.jayud.storage.model.po.*;
import com.jayud.storage.mapper.StorageOutOrderMapper;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.velocity.runtime.directive.contrib.For;
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
public class  StorageOutOrderServiceImpl extends ServiceImpl<StorageOutOrderMapper, StorageOutOrder> implements IStorageOutOrderService {

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IStorageOrderService storageOrderService;

    @Autowired
    private IWarehouseAreaShelvesLocationService warehouseAreaShelvesLocationService;

    @Override
    public String createOrder(StorageOutOrderForm storageOutOrderForm) {
        //无论驳回还是草稿在提交，都先删除原来的商品信息
        if(storageOutOrderForm.getId()!=null){
            warehouseGoodsService.deleteWarehouseGoodsFormsByOrderId(storageOutOrderForm.getId());
        }
        StorageOutOrder storageOutOrder = ConvertUtil.convert(storageOutOrderForm, StorageOutOrder.class);
        if(storageOutOrder.getId() == null){
            storageOutOrder.setCreateTime(LocalDateTime.now());
            storageOutOrder.setCreateUser(UserOperator.getToken());
            storageOutOrder.setStatus("CCE_0");
            this.save(storageOutOrder);
        }else{
            storageOutOrder.setId(storageOutOrderForm.getId());
            storageOutOrder.setCreateTime(LocalDateTime.now());
            storageOutOrder.setCreateUser(UserOperator.getToken());
            storageOutOrder.setStatus("CCE_0");
            this.updateById(storageOutOrder);
        }
        String orderNo = storageOutOrder.getOrderNo();
        if(CollectionUtils.isNotEmpty(storageOutOrderForm.getGoodsFormList())){
            List<WarehouseGoodsForm> goodsFormList = storageOutOrderForm.getGoodsFormList();
            List<WarehouseGoods> warehouseGoods = new ArrayList<>();
            for (WarehouseGoodsForm warehouseGood : goodsFormList) {
                WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                convert.setOrderId(storageOutOrder.getId());
                convert.setOrderNo(storageOutOrder.getOrderNo());
                convert.setCreateTime(LocalDateTime.now());
                convert.setCreateUser(UserOperator.getToken());
                convert.setType(2);
                convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                warehouseGoods.add(convert);

                //出库订单创建成功，锁定库存  如果暂存，不锁定库存
                if(storageOutOrderForm.getCmd().equals("submit")){
                    if(convert.getNumber() != null || convert.getPcs() != null){
                        boolean result = stockService.lockInInventory(convert);
                        if(!result){
                            log.warn(convert.getName() + "锁定库存失败");
                        }
                    }
                }
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
        List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList1(storageOutOrder.getId(),storageOutOrder.getOrderNo());
//        System.out.println("warehouseGoods===================="+warehouseGoods);
        if(CollectionUtils.isEmpty(warehouseGoods)){
            warehouseGoods.add(new WarehouseGoodsVO());
            storageOutOrderVO.setTotalNumberStr("0板0件0pcs");
            storageOutOrderVO.setTotalWeightStr("0KG");
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
            storageOutOrderVO.setTotalNumberStr(borderNumber+"板"+number+"件"+pcs+"pcs");
            storageOutOrderVO.setTotalWeightStr(totalWeight+"KG");
        }
        storageOutOrderVO.setGoodsFormList(warehouseGoods);
        for (WarehouseGoodsVO warehouseGood : warehouseGoods) {
            warehouseGood.setOrderTaker(storageOutOrderVO.getOrderTaker());
        }
        storageOutOrderVO.setWarehouseGoodsVOS(warehouseGoods);
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
        //订单驳回，释放锁定库存，增加库存信息
        boolean result = stockService.changeInventory(storageOutOrder.getOrderNo(),storageOutOrder.getId());
        if(!result){
            log.warn("库存变更失败");
        }
    }

    //入库接单
    @Override
    public void warehouseReceipt(StorageOutProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        StorageOutOrder storageOutOrder = new StorageOutOrder();
        storageOutOrder.setOrderTaker(form.getOperatorUser());
        storageOutOrder.setReceivingOrdersDate(form.getOperatorTime());
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
     * 出库流程操作记录
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
        form.setBusinessType(BusinessTypeEnum.RK.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }

    }

    //打印拣货单
    @Override
    public boolean printPickingList(StorageOutProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        StorageOutOrder storageOutOrder = new StorageOutOrder();
        storageOutOrder.setId(form.getOrderId());
        storageOutOrder.setUpdateUser(UserOperator.getToken());
        storageOutOrder.setUpdateTime(LocalDateTime.now());
        storageOutOrder.setStatus(form.getStatus());
        boolean b = this.updateById(storageOutOrder);
        if(!b){
            log.warn("修改状态失败");
            return false;
        }
        //保存出库库位信息
        List<OutWarehouseGoodsForm> outWarehouseGoodsForms = form.getOutWarehouseGoodsForms();
        for (OutWarehouseGoodsForm outWarehouseGoodsForm : outWarehouseGoodsForms) {
            List<GoodsLocationRecordFormVO> goodsLocationRecordForms = outWarehouseGoodsForm.getGoodsLocationRecordForms();
            for (GoodsLocationRecordFormVO goodsLocationRecordForm : goodsLocationRecordForms) {
                GoodsLocationRecord goodsLocationRecord = ConvertUtil.convert(goodsLocationRecordForm, GoodsLocationRecord.class);
                goodsLocationRecord.setInGoodId(outWarehouseGoodsForm.getId());
                goodsLocationRecord.setCreateTime(LocalDateTime.now());
                goodsLocationRecord.setCreateUser(UserOperator.getToken());
                goodsLocationRecord.setType(2);
                goodsLocationRecord.setIsPickedGoods(1);
                boolean b1 = goodsLocationRecordService.saveOrUpdate(goodsLocationRecord);
                if(!b1){
                    return false;
                }
                //计算该库位该商品的未出库数量
                GoodsLocationRecord goodsLocationRecord1 = goodsLocationRecordService.getGoodsLocationRecordBySkuAndKuCode(goodsLocationRecordForm.getKuCode(),outWarehouseGoodsForm.getWarehousingBatchNo(),outWarehouseGoodsForm.getSku());
                goodsLocationRecord1.setUnDeliveredQuantity(goodsLocationRecord1.getUnDeliveredQuantity() - goodsLocationRecord.getNumber());
                boolean b2 = goodsLocationRecordService.saveOrUpdate(goodsLocationRecord1);
                if(!b2){
                    return false;
                }
            }
        }

        //节点操作记录
        this.storageProcessOptRecord(form);

        //完成订单状态
        finishStorageOrderOpt(storageOutOrder);
        return true;
    }

    //仓储拣货
    @Override
    public boolean warehousePicking(StorageOutProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        StorageOutOrder storageOutOrder = new StorageOutOrder();
        storageOutOrder.setId(form.getOrderId());
        storageOutOrder.setUpdateUser(UserOperator.getToken());
        storageOutOrder.setUpdateTime(LocalDateTime.now());
        storageOutOrder.setStatus(form.getStatus());
        boolean b = this.updateById(storageOutOrder);
        if(!b){
            log.warn("修改状态失败");
            return false;
        }
        //节点操作记录
        this.storageProcessOptRecord(form);

        //完成订单状态
        finishStorageOrderOpt(storageOutOrder);
        return true;
    }

    //出仓异常
    @Override
    public void abnormalOutOfWarehouse(StorageOutProcessOptForm form) {

        form.setOperatorUser(UserOperator.getToken());
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

    //出仓确认
    @Override
    public void confirmDelivery(StorageOutProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
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
            //出仓成功，释放该商品的锁定库存；
            boolean b1 = stockService.releaseInventory(form.getOrderId(),form.getOrderNo());
            if(!b1){
                log.warn("修改状态失败");
            }

            //节点操作记录
            this.storageProcessOptRecord(form);

            //完成订单状态
            finishStorageOrderOpt(storageOutOrder);

            //出仓成功，生成存仓订单
            List<OutWarehouseGoodsForm> outWarehouseGoodsForms = form.getOutWarehouseGoodsForms();
            for (OutWarehouseGoodsForm outWarehouseGoodsForm : outWarehouseGoodsForms) {
                StorageOrder storageOrder = new StorageOrder();
                storageOrder.setOutOrderNo(form.getOrderNo());
                storageOrder.setSku(outWarehouseGoodsForm.getSku());
                storageOrder.setWarehousingBatchNo(outWarehouseGoodsForm.getWarehousingBatchNo());
                storageOrder.setEndTime(storageOutOrder.getUpdateTime());
                boolean b2 = this.storageOrderService.saveStorageOrder(storageOrder);
                if(!b2){
                    log.warn(storageOrder.getSku() +"添加存仓订单失败");
                }
            }


        }
        if(form.getCmd().equals("fail")){
            form.setDescription("确认出库，审核不通过");
            //节点操作记录
            this.storageProcessOptRecord(form);
        }
    }

    @Override
    public List<WarehouseGoodsLocationCodeVO> findByOrderNo(QueryPickUpGoodForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(form.getOrderNo() != null){
            queryWrapper.eq("order_no",form.getOrderNo());
        }
        StorageOutOrder storageOutOrder = this.baseMapper.selectOne(queryWrapper);
        List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList1(storageOutOrder.getId(), storageOutOrder.getOrderNo());
        List<WarehouseGoodsLocationCodeVO> warehouseGoodsLocationVOS = new ArrayList<>();
        for (WarehouseGoodsVO warehouseGoodsVO : list1) {

            List<GoodsLocationRecordFormVO> outGoodsLocationRecordByGoodId = goodsLocationRecordService.getOutGoodsLocationRecordByGoodIdAndPicked(warehouseGoodsVO.getId());
            for (GoodsLocationRecordFormVO goodsLocationRecordFormVO : outGoodsLocationRecordByGoodId) {
                WarehouseGoodsLocationCodeVO warehouseGoodsLocationCodeVO = new WarehouseGoodsLocationCodeVO();
                warehouseGoodsLocationCodeVO.setName(warehouseGoodsVO.getName());
                warehouseGoodsLocationCodeVO.setSku(warehouseGoodsVO.getSku());
                warehouseGoodsLocationCodeVO.setNumber(goodsLocationRecordFormVO.getNumber());
                warehouseGoodsLocationCodeVO.setWarehousingBatchNo(warehouseGoodsVO.getWarehousingBatchNo());
                warehouseGoodsLocationCodeVO.setExpectedDeliveryTime(warehouseGoodsVO.getExpectedDeliveryTime());
                warehouseGoodsLocationCodeVO.setSpecificationModel(warehouseGoodsVO.getSpecificationModel());
                warehouseGoodsLocationCodeVO.setKuCode(goodsLocationRecordFormVO.getKuCode());
                warehouseGoodsLocationCodeVO.setId(warehouseGoodsVO.getId());
                warehouseGoodsLocationVOS.add(warehouseGoodsLocationCodeVO);
            }

        }
        return warehouseGoodsLocationVOS;
    }

    //PDA拣货
    @Override
    public boolean PDAWarehousePicking(WarehousePickingForm form) {
        //拣货成功，修改拣货状态
        GoodsLocationRecord outListByKuCodeAndInGoodId = goodsLocationRecordService.getOutListByKuCodeAndInGoodId(form.getId(),form.getKuCode());
        outListByKuCodeAndInGoodId.setUnDeliveredQuantity(form.getPickedNumber());
        if(outListByKuCodeAndInGoodId.getNumber().equals(outListByKuCodeAndInGoodId.getUnDeliveredQuantity())){
            outListByKuCodeAndInGoodId.setIsPickedGoods(2);
            boolean update = goodsLocationRecordService.saveOrUpdate(outListByKuCodeAndInGoodId);
            if(!update){
                return false;
            }
        }else{
            boolean update = goodsLocationRecordService.saveOrUpdate(outListByKuCodeAndInGoodId);
            if(!update){
                return false;
            }
        }


        if(form.getCmd().equals("end")){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no",form.getOrderNo());
            StorageOutOrder one = this.getOne(queryWrapper);
            one.setStatus("CCE_3");
            one.setUpdateUser(UserOperator.getToken());
            one.setUpdateTime(LocalDateTime.now());
            boolean update1 = this.saveOrUpdate(one);
            if(!update1){
                return false;
            }
        }

        return true;
    }


    @Override
    public List<OnShelfOrderVO> getListByQueryForm(QueryPutGoodForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","CCE_2");
        if(form.getOrderNo() != null){
            queryWrapper.like("order_no",form.getOrderNo());
        }
        List<StorageOutOrder> list = this.baseMapper.selectList(queryWrapper);
        List<OnShelfOrderVO> onShelfOrderVOS = new ArrayList<>();
        for (StorageOutOrder storageOutOrder : list) {
            List<OnShelfOrderVO> onShelfOrderVOS1 = warehouseGoodsService.getListByOrderIdAndTime2(storageOutOrder.getId(),storageOutOrder.getOrderNo(),form.getStartTime(),form.getEndTime());
            for (OnShelfOrderVO inGoodsOperationRecord : onShelfOrderVOS1) {
                onShelfOrderVOS.add(inGoodsOperationRecord);
            }
        }
        return onShelfOrderVOS;
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
