package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageFastOrder;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.model.vo.StorageFastOrderFormVO;
import com.jayud.storage.model.vo.StorageOutOrderFormVO;
import com.jayud.storage.service.IStorageFastOrderService;
import com.jayud.storage.service.IWarehouseGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 快进快出订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
@RestController
@Api("快进快出订单")
@RequestMapping("/storageFastOrder")
@Slf4j
public class StorageFastOrderController {

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private IStorageFastOrderService storageFastOrderService;

    @ApiOperation("分页查询快进快出订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStorageFastOrderForm form) {

        //费用审核，获取所有在子订单录入费用的仓储出库订单数据
        if(form.getCmd() != null && form.getCmd().equals("costAudit")){
            List<String> cci = omsClient.getReceivableCost("ccf").getData();
            List<String> cci1 = omsClient.getPaymentCost("ccf").getData();
            if(CollectionUtils.isEmpty(cci)){
                cci.add(null);
            }
            if(CollectionUtils.isEmpty(cci1)){
                cci1.add(null);
            }
            form.setSubPaymentOrderNos(cci1);
            form.setSubReceviableOrderNos(cci);
        }

        form.setStartTime();
        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyMainOrderNo(mainOrders);
            } else {
                form.setMainOrderNos(Collections.singletonList("-1"));
            }
        }

        List list = new ArrayList();
        //获取表头信息
//        Field[] declaredFields = new Field[100];

        Class<StorageFastOrderFormVO> storageFastOrderFormVOClass = StorageFastOrderFormVO.class;
        Field[] declaredFields = storageFastOrderFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                Map map = new HashMap<>();
                map.put("key", declaredField.getName());
                map.put("name", annotation.value());
                list.add(map);
            }
        }


        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<StorageFastOrderFormVO> page = this.storageFastOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<StorageFastOrderFormVO> records = page.getRecords();
        List<Long> storageOutOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageFastOrderFormVO record : records) {
            storageOutOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
            subOrderNos.add(record.getOrderNo());
        }

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.CCF.getSignOne()).getData();

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageFastOrderFormVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装商品信息
            if(record.getIsWarehouse().equals(0)){
                record.assemblyGoodsInfo(warehouseGoodsService.getList(record.getId(),record.getOrderNo(),3));
                record.setFastGoodsFormList(warehouseGoodsService.getList(record.getId(),record.getOrderNo(),3));
            }else{
                record.assemblyGoodsInfo(warehouseGoodsService.getList(record.getId(),record.getOrderNo(),4));
                record.setInGoodsFormList(warehouseGoodsService.getList(record.getId(),record.getOrderNo(),4));
                record.setOutGoodsFormList(warehouseGoodsService.getList(record.getId(),record.getOrderNo(),5));
            }


            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            record.setCost(MapUtil.getBool(data, record.getOrderNo()));
            record.setLicensePlate((record.getOutPlateNumber()==null ? "":record.getOutPlateNumber()));
            record.setTakeTimeStr((record.getExpectedDeliveryTime().toString()==null ? "":record.getExpectedDeliveryTime().toString()));
            record.setCreatedTimeStr(record.getCreateTime().toString());
            record.setSubLegalName(record.getLegalName());
            record.setOrderId(record.getId());
            record.setSubUnitCode(record.getUnitCode());
            record.setDefaultUnitCode(record.getUnitCode());

        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    @ApiOperation(value = "执行快进快出流程操作")
    @PostMapping(value = "/doStorageInProcessOpt")
    public CommonResult doStorageInProcessOpt(@RequestBody @Valid StorageFastProcessOptForm form, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主订单id/入库订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //入库订单信息
        StorageFastOrder storageFastOrder = this.storageFastOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(storageFastOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(storageFastOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }

        if(storageFastOrder.getIsWarehouse().equals(0)){
            storageFastOrderService.ordersReceived(form);
            return CommonResult.success();
        }else{
            String orderProcessNode = (String) omsClient.getOrderProcessNode(storageFastOrder.getMainOrderNo(), storageFastOrder.getOrderNo(), storageFastOrder.getStatus()).getData();
            OrderStatusEnum statusEnum = OrderStatusEnum.getStorageFastOrderNextStatus(orderProcessNode);
            if (statusEnum == null) {
                log.error("执行入库流程操作失败,超出流程之外 data={}", storageFastOrder);
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }

            //校验参数
            form.checkProcessOpt(statusEnum);
            form.setStatus(statusEnum.getCode());

            //指令操作
            switch (statusEnum) {
                case CCF_1: //快进快出接单
                    storageFastOrderService.confirmReceipt(form);
                    break;
                case CCF_2: //入库确认
//                    boolean b = storageInputOrderService.confirmEntry(form);
//                    if(!b){
//                        CommonResult.error(443,"确认入仓失败");
//                    }
                    break;
                case CCF_3: //入库完结
//                    boolean a = storageInputOrderService.warehousingEntry(form);
//                    if(!a){
//                        CommonResult.error(443,"仓储入库失败");
//                    }
                    break;
            }

            return CommonResult.success();
        }
    }

    @ApiOperation(value = "快进快出订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody StorageOutCargoRejected storageOutCargoRejected) {
        //查询拖车订单
        StorageFastOrder tmp = this.storageFastOrderService.getById(storageOutCargoRejected.getStorageInOrderId());
        //获取相应驳回操作
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getOutStorageOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(storageOutCargoRejected.getStorageInOrderId());
        auditInfoForm.setExtDesc(SqlConstant.STORAGE_OUT_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(storageOutCargoRejected.getCause());

        switch (orderStatusEnum) {
            case CCF_1_1:
                //订单驳回
                this.storageFastOrderService.orderReceiving(tmp, auditInfoForm, storageOutCargoRejected);
                break;
        }

        return CommonResult.success();
    }

}

