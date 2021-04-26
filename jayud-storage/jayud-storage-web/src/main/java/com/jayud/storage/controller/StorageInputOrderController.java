package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.bo.StorageInProcessOptForm;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.po.StorageInputOrderDetails;
import com.jayud.storage.model.vo.StorageInProcessOptFormVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.WarehouseGoodsVO;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStorageInputOrderDetailsService;
import com.jayud.storage.service.IStorageInputOrderService;
import com.jayud.storage.service.IWarehouseGoodsService;
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
 * 入库订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@Slf4j
@RequestMapping("/storageInputOrder")
public class StorageInputOrderController {

    @Autowired
    private IStorageInputOrderService storageInputOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private IStorageInputOrderDetailsService storageInputOrderDetailsService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @ApiOperation("分页查询入库订单列表")
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

        Class<StorageInputOrderFormVO> storageInputOrderVOClass = StorageInputOrderFormVO.class;
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
        IPage<StorageInputOrderFormVO> page = this.storageInputOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<StorageInputOrderFormVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageInputOrderFormVO record : records) {
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
        for (StorageInputOrderFormVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    @ApiOperation(value = "执行入库流程操作")
    @PostMapping(value = "/doStorageInProcessOpt")
    public CommonResult doTrailerProcessOpt(@RequestBody @Valid StorageInProcessOptForm form, BindingResult result) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主订单id/入库订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //拖车订单信息
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(storageInputOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(storageInputOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        String orderProcessNode = (String) omsClient.getOrderProcessNode(storageInputOrder.getMainOrderNo(), storageInputOrder.getOrderNo(), storageInputOrder.getStatus()).getData();

        OrderStatusEnum statusEnum = OrderStatusEnum.getTrailerOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("执行拖车流程操作失败,超出流程之外 data={}", storageInputOrder);
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //校验参数
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //指令操作
        switch (statusEnum) {
            case CCI_1: //入库接单
                storageInputOrderService.warehouseReceipt(form);
                break;
            case CCI_2: //确认入仓
                storageInputOrderService.confirmEntry(form);
                break;
            case CCI_3: //仓储入库
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "获取入仓时的数据")
    @PostMapping(value = "/getDataInConfirmEntry")
    public CommonResult getDataInConfirmEntry(@RequestBody Map<String,Object> map){
        Long orderId = MapUtil.getLong(map, "orderId");
        StorageInputOrder storageInputOrder = storageInputOrderService.getById(orderId);
        StorageInputOrderDetails storageInputOrderDetails = storageInputOrderDetailsService.getStorageInputOrderDetails(orderId);
        if(storageInputOrderDetails == null){
            storageInputOrderDetails = new StorageInputOrderDetails();
        }

        StorageInProcessOptFormVO storageInProcessOptFormVO = ConvertUtil.convert(storageInputOrderDetails, StorageInProcessOptFormVO.class);
        List<WarehouseGoodsVO> list = warehouseGoodsService.getList(storageInputOrder.getId(), storageInputOrder.getOrderNo());
        storageInProcessOptFormVO.setWarehouseGoodsForms(list);

        for (WarehouseGoodsVO warehouseGoodsVO : list) {
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getList(storageInputOrder.getId(), storageInputOrder.getOrderNo(),warehouseGoodsVO.getName());
            Double totalWeight = 0.0;
            Integer totalAmount = 0;
            Integer totalJAmount = 0;
            Integer totalPCS = 0;

            for (InGoodsOperationRecord inGoodsOperationRecord : inGoodsOperationRecords) {
                totalAmount = totalAmount + inGoodsOperationRecord.getNumber();
                totalJAmount = totalJAmount + inGoodsOperationRecord.getBoardNumber();
                totalPCS = totalPCS + inGoodsOperationRecord.getPcs();
                totalWeight = totalWeight + inGoodsOperationRecord.getWeight();
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(totalJAmount).append("板").append("/")
                    .append(totalAmount).append("件").append("/")
                    .append(totalPCS).append("PCS").append("/")
                    .append(totalWeight).append("KG");
            warehouseGoodsVO.setWarehousingInformation(stringBuffer.toString());
        }

        return CommonResult.success();
    }

}

