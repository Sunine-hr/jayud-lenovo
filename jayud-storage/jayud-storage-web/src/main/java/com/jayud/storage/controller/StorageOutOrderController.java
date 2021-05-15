package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.KafkaMsgEnums;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStorageOutOrderService;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
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

    @ApiOperation("分页查询出库订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStorageOrderForm form) {

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
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageOutOrderFormVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
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

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageOutOrderFormVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            record.setCreatedTimeStr(record.getCreateTime().toString());
            record.setSubLegalName(record.getLegalName());

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

        OrderStatusEnum statusEnum = OrderStatusEnum.getStorageInOrderNextStatus(orderProcessNode);
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
                    number = number + goodsLocationRecordForm.getNumber();
                    GoodsLocationRecord goodsLocationRecord = goodsLocationRecordService.getListByGoodIdAndKuId(goodsLocationRecordForm.getIngoodId(),goodsLocationRecordForm.getKuId());
                    if(goodsLocationRecord.getNumber()<goodsLocationRecordForm.getNumber()){//填的商品超过了该库位的总商品数
                        //通过库位id获取库位名字
                        WarehouseAreaShelvesLocation warehouseAreaShelvesLocation = warehouseAreaShelvesLocationService.getById(goodsLocationRecordForm.getKuId());
                        return CommonResult.error(400, warehouseAreaShelvesLocation+"的该商品最大数量为"+goodsLocationRecord.getNumber());
                    }
                }
                if(outWarehouseGoodsForm.getNumber()!=number){
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
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageOutOrderVO.getMainOrderNo()));
        convert.assemblyMainOrderData(result);
        convert.assemblyPickingListData(storageOutOrderVO.getGoodsFormList());
        List<WarehouseGoodsLocationVO> goodsFormList = new ArrayList<>();
        for (WarehouseGoodsVO warehouseGoodsVO : storageOutOrderVO.getGoodsFormList()) {
            WarehouseGoodsLocationVO warehouseGoodsLocationVO = ConvertUtil.convert(warehouseGoodsVO, WarehouseGoodsLocationVO.class);
            List<GoodsLocationRecord> goodsLocationRecords = goodsLocationRecordService.getListByGoodId(warehouseGoodsVO.getId(),warehouseGoodsVO.getOrderId(),warehouseGoodsVO.getSku());
            Integer number = warehouseGoodsVO.getNumber();
            List<GoodsLocationRecordFormVO> goodsLocationRecordFormVOS = new ArrayList<>();
            //循环
            for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecords) {
                //数量小于这个库位的数量，循环结束，
                if(goodsLocationRecord.getNumber()>=number){
                    GoodsLocationRecordFormVO goodsLocationRecordFormVO = new GoodsLocationRecordFormVO();
                    goodsLocationRecordFormVO.setKuId(goodsLocationRecord.getKuId());
                    goodsLocationRecordFormVO.setIngoodId(goodsLocationRecord.getInGoodId());
                    goodsLocationRecordFormVO.setNumber(number);
                    goodsLocationRecordFormVOS.add(goodsLocationRecordFormVO);
                    break;
                }else if(goodsLocationRecord.getNumber()<number){
                    GoodsLocationRecordFormVO goodsLocationRecordFormVO = new GoodsLocationRecordFormVO();
                    goodsLocationRecordFormVO.setKuId(goodsLocationRecord.getKuId());
                    goodsLocationRecordFormVO.setIngoodId(goodsLocationRecord.getInGoodId());
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
        convert.setGoodsFormList(goodsFormList);
        return CommonResult.success(convert);
    }

    @ApiOperation(value = "获取对应商品的库位下拉列表框信息")
    @PostMapping(value = "/getLocationComboxByOrderId")
    public CommonResult getLocationComboxByOrderId(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        List<InGoodsOperationRecord> listByOrderId = goodsOperationRecordService.getListByOrderId(id);
        Map map1 = new HashMap();
        for (InGoodsOperationRecord inGoodsOperationRecord : listByOrderId) {
            List<GoodsLocationRecord> goodsLocationRecords = goodsLocationRecordService.getGoodsLocationRecordByGoodId(inGoodsOperationRecord.getId());
            map1.put(inGoodsOperationRecord.getSku(),goodsLocationRecords);
        }
        return CommonResult.success(map1);
    }
}
