package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.*;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.*;
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
 * 出库订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@Slf4j
@RequestMapping("/storageOutOrder")
@Api(tags = "出库订单管理")
public class StorageOutOrderController {

    @Autowired
    private IStorageOutOrderService storageOutOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private IInGoodsOperationRecordService goodsOperationRecordService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private IWarehouseAreaShelvesLocationService warehouseAreaShelvesLocationService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @ApiOperation("分页查询出库订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStorageOrderForm form) {

        //费用审核，获取所有在子订单录入费用的仓储出库订单数据
        if(form.getCmd() != null && form.getCmd().equals("costAudit")){
            List<String> cci = omsClient.getReceivableCost("cce").getData();
            List<String> cci1 = omsClient.getPaymentCost("cce").getData();
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

        Class<StorageOutOrderFormVO> storageInputOrderVOClass = StorageOutOrderFormVO.class;
        Field[] declaredFields = storageInputOrderVOClass.getDeclaredFields();
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
        IPage<StorageOutOrderFormVO> page = this.storageOutOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<StorageOutOrderFormVO> records = page.getRecords();
        List<Long> storageOutOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageOutOrderFormVO record : records) {
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

        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.CCE.getSignOne()).getData();

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageOutOrderFormVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装商品信息
            record.assemblyGoodsInfo(warehouseGoodsService.getList(record.getId(),record.getOrderNo(),2));

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            record.setCost(MapUtil.getBool(data, record.getOrderNo()));

            record.setCreatedTimeStr(record.getCreateTime().toString());
            record.setSubLegalName(record.getLegalName());
            record.setOrderId(record.getId());
            record.setSubUnitCode(record.getUnitCode());
            record.setDefaultUnitCode(record.getUnitCode());

        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    @ApiOperation(value = "执行出库流程操作")
    @PostMapping(value = "/doStorageOutProcessOpt")
    public CommonResult doTrailerProcessOpt(@RequestBody @Valid StorageOutProcessOptForm form, BindingResult result) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主订单id/出库订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //出库订单信息
        StorageOutOrder storageOutOrder = this.storageOutOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(storageOutOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(storageOutOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        String orderProcessNode = (String) omsClient.getOrderProcessNode(storageOutOrder.getMainOrderNo(), storageOutOrder.getOrderNo(), storageOutOrder.getStatus()).getData();

        System.out.println("orderProcessNode==============="+orderProcessNode);

        OrderStatusEnum statusEnum = OrderStatusEnum.getStorageOutOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("执行出库流程操作失败,超出流程之外 data={}", storageOutOrder);
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //校验参数
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        if(form.getStatus().equals(OrderStatusEnum.CCE_2.getCode())){
            List<OutWarehouseGoodsForm> outWarehouseGoodsForms = form.getOutWarehouseGoodsForms();
            for (OutWarehouseGoodsForm outWarehouseGoodsForm : outWarehouseGoodsForms) {
                List<GoodsLocationRecordFormVO> goodsLocationRecordForms = outWarehouseGoodsForm.getGoodsLocationRecordForms();
                Integer number = 0;
                for (GoodsLocationRecordFormVO goodsLocationRecordForm : goodsLocationRecordForms) {
                    if(goodsLocationRecordForm.getNumber() != null){
                        number = number + goodsLocationRecordForm.getNumber();
                    }
                    //入库商品
                    GoodsLocationRecord goodsLocationRecord = goodsLocationRecordService.getGoodsLocationRecordBySkuAndKuCode(goodsLocationRecordForm.getKuCode(),outWarehouseGoodsForm.getWarehousingBatchNo(),outWarehouseGoodsForm.getSku());
                    System.out.println("goodsLocationRecord==============="+goodsLocationRecord);
                    if(goodsLocationRecord == null){
                        return CommonResult.error(400, goodsLocationRecordForm.getKuCode()+"不存在"+outWarehouseGoodsForm.getSku());
                    }
                    if(goodsLocationRecord.getNumber()<goodsLocationRecordForm.getNumber()){//填的商品超过了该库位的总商品数
                        return CommonResult.error(400, goodsLocationRecordForm.getKuCode()+"的该商品最大数量为"+goodsLocationRecord.getNumber());
                    }
                }
                if(!outWarehouseGoodsForm.getNumber().equals(number)){
                    return CommonResult.error(400, "该商品拣货数量与订单不一致");
                }
            }
        }

        //指令操作
        switch (statusEnum) {
            case CCE_1: //出库接单
                storageOutOrderService.warehouseReceipt(form);
                break;
            case CCE_2: //打印拣货单
                boolean b = storageOutOrderService.printPickingList(form);
                if(!b){
                    CommonResult.error(444,"打印拣货单失败");
                }
                break;
            case CCE_3: //仓储拣货
                boolean a = storageOutOrderService.warehousePicking(form);
                if(!a){
                    CommonResult.error(444,"仓储拣货失败");
                }
                break;
            case CCE_4: //出仓确认
                storageOutOrderService.confirmDelivery(form);
                break;
            case CCE_5: //出仓异常
                storageOutOrderService.abnormalOutOfWarehouse(form);
                break;
        }

        return CommonResult.success();
    }



    @ApiOperation(value = "出库订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody StorageOutCargoRejected storageOutCargoRejected) {
        //查询拖车订单
        StorageOutOrder tmp = this.storageOutOrderService.getById(storageOutCargoRejected.getStorageInOrderId());
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
            case CCE_1_1:
                //订单驳回
                this.storageOutOrderService.orderReceiving(tmp, auditInfoForm, storageOutCargoRejected);
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "获取拣货单信息")
    @PostMapping(value = "/getPickingListInformation")
    public CommonResult getPickingListInformation(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        StorageOutOrderVO storageOutOrderVO = storageOutOrderService.getStorageOutOrderVOById(id);
        StorageOutPickingListVO convert = ConvertUtil.convert(storageOutOrderVO, StorageOutPickingListVO.class);
        convert.setOrderId(convert.getId());

        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageOutOrderVO.getMainOrderNo()));
        convert.assemblyMainOrderData(result.getData());
        convert.assemblyPickingListData(storageOutOrderVO.getGoodsFormList());

        List<WarehouseGoodsLocationVO> goodsFormList = new ArrayList<>();
        for (WarehouseGoodsVO warehouseGoodsVO : storageOutOrderVO.getGoodsFormList()) {
            WarehouseGoodsLocationVO warehouseGoodsLocationVO = ConvertUtil.convert(warehouseGoodsVO, WarehouseGoodsLocationVO.class);
            //获取该商品所在的库位
            List<GoodsLocationRecord> goodsLocationRecords = goodsLocationRecordService.getListByGoodId(warehouseGoodsVO.getWarehousingBatchNo(),warehouseGoodsVO.getSku());
            Integer number = warehouseGoodsVO.getNumber();
            List<GoodsLocationRecordFormVO> goodsLocationRecordFormVOS = new ArrayList<>();
            //循环
            for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecords) {
                //数量小于这个库位的数量，循环结束，
                if(goodsLocationRecord.getNumber()>=number){
                    GoodsLocationRecordFormVO goodsLocationRecordFormVO = new GoodsLocationRecordFormVO();
                    goodsLocationRecordFormVO.setKuCode(goodsLocationRecord.getKuCode());
                    goodsLocationRecordFormVO.setInGoodId(goodsLocationRecord.getInGoodId());
                    goodsLocationRecordFormVO.setNumber(number);
                    goodsLocationRecordFormVOS.add(goodsLocationRecordFormVO);
                    break;
                }else if(goodsLocationRecord.getNumber()<number){
                    GoodsLocationRecordFormVO goodsLocationRecordFormVO = new GoodsLocationRecordFormVO();
                    goodsLocationRecordFormVO.setKuCode(goodsLocationRecord.getKuCode());
                    goodsLocationRecordFormVO.setInGoodId(goodsLocationRecord.getInGoodId());
                    goodsLocationRecordFormVO.setNumber(goodsLocationRecord.getNumber());
                    goodsLocationRecordFormVOS.add(goodsLocationRecordFormVO);
                }
                number = number - goodsLocationRecord.getNumber();
                if(number<=0){
                    break;
                }
            }
            warehouseGoodsLocationVO.setGoodsLocationRecordForms(goodsLocationRecordFormVOS);
            goodsFormList.add(warehouseGoodsLocationVO);
        }
        convert.setOutWarehouseGoodsForms(goodsFormList);
        return CommonResult.success(convert);
    }

    @ApiOperation(value = "获取仓储拣货信息")
    @PostMapping(value = "/getWarehousePickingInformation")
    public CommonResult getWarehousePickingInformation(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        StorageOutOrderVO storageOutOrderVO = storageOutOrderService.getStorageOutOrderVOById(id);
        StorageOutPickingListVO convert = ConvertUtil.convert(storageOutOrderVO, StorageOutPickingListVO.class);
        convert.setOrderId(convert.getId());

        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageOutOrderVO.getMainOrderNo()));
        convert.assemblyMainOrderData(result.getData());
        convert.assemblyPickingListData(storageOutOrderVO.getGoodsFormList());
        List<WarehouseGoodsLocationVO> goodsFormList = new ArrayList<>();
        for (WarehouseGoodsVO warehouseGoodsVO : storageOutOrderVO.getGoodsFormList()) {
            WarehouseGoodsLocationVO warehouseGoodsLocationVO = ConvertUtil.convert(warehouseGoodsVO, WarehouseGoodsLocationVO.class);
            List<GoodsLocationRecordFormVO> goodsLocationRecordFormVOS = goodsLocationRecordService.getOutGoodsLocationRecordByGoodId(warehouseGoodsLocationVO.getId());
            System.out.println("goodsLocationRecordFormVOS============================="+goodsLocationRecordFormVOS);
            warehouseGoodsLocationVO.setGoodsLocationRecordForms(goodsLocationRecordFormVOS);
            goodsFormList.add(warehouseGoodsLocationVO);
        }
        convert.setOutWarehouseGoodsForms(goodsFormList);
        return CommonResult.success(convert);
    }

    @ApiOperation(value = "获取对应商品的库位下拉列表框信息")
    @PostMapping(value = "/getLocationComboxByOrderId")
    public CommonResult getLocationComboxByOrderId(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        //获取实际入库记录
        List<InGoodsOperationRecord> listByOrderId = goodsOperationRecordService.getListByOrderId(id);
        Map map1 = new HashMap();
        for (InGoodsOperationRecord inGoodsOperationRecord : listByOrderId) {
            //获取入库的实际库位
            List<GoodsLocationRecord> goodsLocationRecords = goodsLocationRecordService.getGoodsLocationRecordByGoodId(inGoodsOperationRecord.getId());
            map1.put(inGoodsOperationRecord.getSku(),goodsLocationRecords);
        }
        return CommonResult.success(map1);
    }
}

