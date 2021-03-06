package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;

import cn.hutool.json.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@Slf4j
@Api(tags = "??????????????????")
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

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private IWarehouseService warehouseService;

    @ApiOperation("??????????????????????????????")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStorageOrderForm form) {

        //??????????????????????????????????????????????????????????????????????????????
        if(form.getCmd() != null && form.getCmd().equals("costAudit")){
            List<String> cci = omsClient.getReceivableCost("cci").getData();
            List<String> cci1 = omsClient.getPaymentCost("cci").getData();
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
        //????????????????????????
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
        //??????????????????
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
        //??????????????????
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<StorageInputOrderFormVO> records = page.getRecords();
        List<Long> storageInputOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        List<Long> departmentIds = new ArrayList<>();
        for (StorageInputOrderFormVO record : records) {
            storageInputOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
            subOrderNos.add(record.getOrderNo());
            departmentIds.add(record.getDepartmentId());
        }

        //??????????????????
        ApiResult departmentResult = null;
        if(CollectionUtils.isNotEmpty(departmentIds)){
            departmentResult = this.oauthClient.getDepartmentByDepartment(departmentIds);
        }

        //??????????????????
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //????????????????????????
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.CCI.getSignOne()).getData();

        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageInputOrderFormVO record : records) {
            record.setOrderId(record.getId());
            //?????????????????????
            record.assemblyMainOrderData(result.getData());
            //??????????????????
            record.assemblyLegalEntity(legalEntityResult);

            record.assemblyDepartment(departmentResult);

            record.assembleCostStatus(record.getOrderNo(),
                    this.omsClient.getCostStatus(null, Collections.singletonList(record.getOrderNo())).getData());

            //??????????????????
            record.assemblyUnitCodeInfo(unitCodeInfo);

            record.setCost(MapUtil.getBool(data, record.getOrderNo()));

            record.setCreatedTimeStr(record.getCreateTime().toString().replace("T"," "));
            record.setSubLegalName(record.getLegalName());
            record.setSubUnitCode(record.getUnitCode());
            record.setDefaultUnitCode(record.getUnitCode());

            List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList(record.getId(), record.getOrderNo(),1);
            record.assemblyGoodsInfo(list1);
            //????????????????????????????????????
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getListByOrderId(record.getId(), record.getOrderNo());
            record.assemblyGoodsInfo1(inGoodsOperationRecords);

        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }


    @ApiOperation("????????????????????????????????????")
    @PostMapping("/findWarehousingByPage")
    public CommonResult findWarehousingByPage(@RequestBody QueryStorageOrderForm form) {
        form.setStartTime();
        //????????????????????????
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
        //??????????????????

        Class<StorageInputOrderWarehouseingVO> storageInputOrderVOClass = StorageInputOrderWarehouseingVO.class;
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
        IPage<StorageInputOrderWarehouseingVO> page = this.storageInputOrderService.findWarehousingByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        List<StorageInputOrderWarehouseingVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageInputOrderWarehouseingVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
        }

        //??????????????????
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //????????????????????????
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        String path = (String)fileClient.getBaseUrl().getData();

        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageInputOrderWarehouseingVO record : records) {
            record.setOrderId(record.getId());
            //?????????????????????
            record.assemblyMainOrderData(result.getData());
            //??????????????????
            record.assemblyLegalEntity(legalEntityResult);

            //??????????????????
            record.assemblyUnitCodeInfo(unitCodeInfo);
            //????????????????????????
            List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList(record.getId(), record.getOrderNo(),1);
            for (WarehouseGoodsVO warehouseGoodsVO : list1) {
                warehouseGoodsVO.setTakeFiles(StringUtils.getFileViews(warehouseGoodsVO.getFileName(),warehouseGoodsVO.getFilePath(),path));
            }
            record.assemblyGoodsInfo(list1);
            //????????????????????????????????????
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getListByWarehousingBatchNo(record.getWarehousingBatchNo());
            record.assemblyGoodsInfo1(inGoodsOperationRecords);
        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }



    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/doStorageInProcessOpt")
    public CommonResult doStorageInProcessOpt(@RequestBody @Valid StorageInProcessOptForm form, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("?????????id/????????????id??????");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //??????????????????
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(storageInputOrder.getProcessStatus())) {
            return CommonResult.error(400, "?????????????????????");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(storageInputOrder.getProcessStatus())) {
            return CommonResult.error(400, "????????????????????????");
        }
        String orderProcessNode = (String) omsClient.getOrderProcessNode(storageInputOrder.getMainOrderNo(), storageInputOrder.getOrderNo(), storageInputOrder.getStatus()).getData();


        OrderStatusEnum statusEnum = OrderStatusEnum.getStorageInOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("??????????????????????????????,?????????????????? data={}", storageInputOrder);
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //????????????
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        if(form.getStatus().equals(OrderStatusEnum.CCI_2.getCode()) && form.getCmd().equals("submit")){
            List<WarehouseGoodsForm> warehouseGoodsForms = form.getWarehouseGoodsForms();
            if(CollectionUtils.isEmpty(warehouseGoodsForms)){
                return CommonResult.error(443,"??????????????????");
            } else{
                for (WarehouseGoodsForm warehouseGoodsForm : warehouseGoodsForms) {
                    if(warehouseGoodsForm.getNumber() == null){
                        return CommonResult.error(443,"????????????????????????");
                    }

                    if(warehouseGoodsForm.getSjBoardNumber() != null || warehouseGoodsForm.getSjVolume() != null
                    || warehouseGoodsForm.getSjWeight() != null || warehouseGoodsForm.getSjPcs() != null){
                        if(warehouseGoodsForm.getSjNumber() == null){
                            return CommonResult.error(443,"?????????????????????????????????");
                        }
                    }
                }

            }

        }

        if(form.getStatus().equals(OrderStatusEnum.CCI_2.getCode()) && form.getCmd().equals("end") && form.getIsOver()){
            for (WarehouseGoodsForm warehouseGoodsForm : form.getWarehouseGoodsForms()) {
                List<InGoodsOperationRecord> listByOrderId = inGoodsOperationRecordService.getListByOrderId(warehouseGoodsForm.getOrderId(), warehouseGoodsForm.getOrderNo());
                if(CollectionUtils.isEmpty(listByOrderId)){
                    return CommonResult.error(443,"????????????????????????????????????????????????????????????????????????");
                }
                List<InGoodsOperationRecord> list = inGoodsOperationRecordService.getList(warehouseGoodsForm.getOrderId(), warehouseGoodsForm.getOrderNo(), warehouseGoodsForm.getSku());
                Integer number = 0;
                for (InGoodsOperationRecord inGoodsOperationRecord : list) {
                    number = number + inGoodsOperationRecord.getNumber();
                }
                if(warehouseGoodsForm.getSjNumber() != null){
                    number = number + warehouseGoodsForm.getSjNumber();
                }
                if(warehouseGoodsForm.getNumber() != number){
                    return CommonResult.error(444,"?????????????????????");
                }
            }
        }
        if(form.getStatus().equals(OrderStatusEnum.CCI_3.getCode())){
            if(this.isWarehousing(form).getCode().equals(443)){
                return CommonResult.error(443,"?????????????????????");
            }
        }

        //????????????
        switch (statusEnum) {
            case CCI_1: //????????????
                storageInputOrderService.warehouseReceipt(form);
                break;
            case CCI_2: //????????????
                boolean b = storageInputOrderService.confirmEntry(form);
                if(!b){
                    CommonResult.error(443,"??????????????????");
                }
                break;
            case CCI_3: //????????????
                boolean a = storageInputOrderService.warehousingEntry(form);
                if(!a){
                    CommonResult.error(443,"??????????????????");
                }
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/getDataInConfirmEntry")
    public CommonResult getDataInConfirmEntry(@RequestBody Map<String,Object> map){
        Long orderId = MapUtil.getLong(map, "orderId");
        StorageInputOrder storageInputOrder = storageInputOrderService.getById(orderId);
        StorageInputOrderDetails storageInputOrderDetails = storageInputOrderDetailsService.getStorageInputOrderDetails(orderId);
        if(storageInputOrderDetails == null){
            storageInputOrderDetails = new StorageInputOrderDetails();
        }

        StorageInProcessOptFormVO storageInProcessOptFormVO = ConvertUtil.convert(storageInputOrderDetails, StorageInProcessOptFormVO.class);

        List<Long> longs = new ArrayList<>();
        List<Long> list1 = new ArrayList<>();
        if(storageInputOrderDetails.getCardTypeId()!=null){
            String[] split = storageInputOrderDetails.getCardTypeId().split(",");
            for (String s : split) {
                longs.add(Long.parseLong(s));
            }
        }
       if(storageInputOrderDetails.getOperationId()!=null){
           String[] split1 = storageInputOrderDetails.getOperationId().split(",");
           for (String s : split1) {
               list1.add(Long.parseLong(s));
           }
       }

        storageInProcessOptFormVO.setCardTypeId(longs);
        storageInProcessOptFormVO.setOperationId(list1);
        List<WarehouseGoodsVO> list = warehouseGoodsService.getList(storageInputOrder.getId(), storageInputOrder.getOrderNo(),1);
        storageInProcessOptFormVO.setWarehouseGoodsForms(list);

        for (WarehouseGoodsVO warehouseGoodsVO : list) {
            warehouseGoodsVO.setIsSubmit(storageInputOrder.getIsSubmit());
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getList(storageInputOrder.getId(), storageInputOrder.getOrderNo(),warehouseGoodsVO.getSku());
            Double totalWeight = 0.0;
            Integer totalAmount = 0;
            Integer totalJAmount = 0;
            Integer totalPCS = 0;

            for (InGoodsOperationRecord inGoodsOperationRecord : inGoodsOperationRecords) {
                totalAmount = totalAmount + (inGoodsOperationRecord.getNumber()==null ? 0 : inGoodsOperationRecord.getNumber());
                totalJAmount = totalJAmount + (inGoodsOperationRecord.getBoardNumber()==null ? 0 : inGoodsOperationRecord.getBoardNumber());
                totalPCS = totalPCS + (inGoodsOperationRecord.getPcs()==null ? 0 : inGoodsOperationRecord.getPcs());
                totalWeight = totalWeight + (inGoodsOperationRecord.getWeight()==null ? 0 : inGoodsOperationRecord.getWeight());
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(totalJAmount).append("???").append("/")
                    .append(totalAmount).append("???").append("/")
                    .append(totalPCS).append("PCS").append("/")
                    .append(totalWeight).append("KG");
            warehouseGoodsVO.setWarehousingInformation(stringBuffer.toString());
        }
        storageInProcessOptFormVO.setWarehouseGoodsForms(list);
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageInputOrder.getMainOrderNo()));
        storageInProcessOptFormVO.setMainOrderNo(storageInputOrder.getMainOrderNo());
        storageInProcessOptFormVO.setOrderId(storageInputOrder.getId());
        storageInProcessOptFormVO.setOrderNo(storageInputOrder.getOrderNo());
        storageInProcessOptFormVO.assemblyMainOrderData(result.getData());
        storageInProcessOptFormVO.setLegalName(storageInputOrder.getLegalName() );

        //??????????????????????????????
        List<InGoodsOperationRecord> listByOrderId = inGoodsOperationRecordService.getListByOrderId(storageInputOrder.getId(), storageInputOrder.getOrderNo());
        if(CollectionUtils.isNotEmpty(listByOrderId)){
            storageInProcessOptFormVO.setInGoodsOperationRecords(ConvertUtil.convertList(listByOrderId,InGoodsOperationRecordVO.class));
        }

//        List<InitComboxWarehouseVO> data2 = omsClient.initComboxWarehouseVO().getData();
//        for (InitComboxWarehouseVO initComboxWarehouseVO : data2) {
//            if(initComboxWarehouseVO.getId().equals(storageInputOrderDetails.getWarehouseId())){
//                storageInProcessOptFormVO.setWarehouseName(initComboxWarehouseVO.getName());
//                storageInProcessOptFormVO.setWarehousePhone(initComboxWarehouseVO.getPhone());
//                storageInProcessOptFormVO.setWarehouseAddress(initComboxWarehouseVO.getAddress());
//            }
//        }
        if(storageInputOrderDetails.getWarehouseId() != null){
            WarehouseVO warehouseById = warehouseService.findWarehouseById(storageInputOrderDetails.getWarehouseId());
            storageInProcessOptFormVO.setWarehouseName(warehouseById.getName());
            storageInProcessOptFormVO.setWarehousePhone(warehouseById.getPhone());
            storageInProcessOptFormVO.setWarehouseAddress(warehouseById.getAddress());
        }


        storageInProcessOptFormVO.setCustomerId(omsClient.getCustomerByCode(storageInProcessOptFormVO.getCustomerCode()).getData());
        storageInProcessOptFormVO.setPlateNumber(storageInputOrder.getPlateNumber());
        storageInProcessOptFormVO.setWarehouseNumber(storageInputOrder.getWarehouseNumber());
        storageInProcessOptFormVO.setCreateTime(storageInputOrder.getCreateTime());
        storageInProcessOptFormVO.setWarehousingBatchNo(redisUtils.get(storageInputOrder.getOrderNo()));
        storageInProcessOptFormVO.setIsSubmit(storageInputOrder.getIsSubmit());
        return CommonResult.success(storageInProcessOptFormVO);
    }

    @ApiOperation(value = "??????????????????????????????")
    @PostMapping(value = "/getDataInWarehousing")
    public CommonResult getDataInWarehousing(@RequestBody Map<String,Object> map){
        Long orderId = MapUtil.getLong(map, "orderId");
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        StorageInputOrder storageInputOrder = storageInputOrderService.getById(orderId);
        StorageInputOrderDetails storageInputOrderDetails = storageInputOrderDetailsService.getStorageInputOrderDetails(orderId);
        if(storageInputOrderDetails == null){
            storageInputOrderDetails = new StorageInputOrderDetails();
        }


        StorageInProcessOptFormVO storageInProcessOptFormVO = ConvertUtil.convert(storageInputOrderDetails, StorageInProcessOptFormVO.class);
        storageInProcessOptFormVO.setWarehousingBatchNo(warehousingBatchNo);
        List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getListByWarehousingBatchNo(warehousingBatchNo);
        List<InGoodsOperationRecordVO> inGoodsOperationRecordVOS = ConvertUtil.convertList(inGoodsOperationRecords, InGoodsOperationRecordVO.class);
        System.out.println("inGoodsOperationRecordVOS============="+inGoodsOperationRecordVOS);
        storageInProcessOptFormVO.setInGoodsOperationRecords(inGoodsOperationRecordVOS);

//        List<InitComboxWarehouseVO> data2 = omsClient.initComboxWarehouseVO().getData();
//        for (InitComboxWarehouseVO initComboxWarehouseVO : data2) {
//            if(initComboxWarehouseVO.getId().equals(storageInputOrderDetails.getWarehouseId())){
//                storageInProcessOptFormVO.setWarehouseName(initComboxWarehouseVO.getName());
//                storageInProcessOptFormVO.setWarehousePhone(initComboxWarehouseVO.getPhone());
//                storageInProcessOptFormVO.setWarehouseAddress(initComboxWarehouseVO.getAddress());
//            }
//        }
        if(storageInputOrderDetails.getWarehouseId() != null){
            WarehouseVO warehouseById = warehouseService.findWarehouseById(storageInputOrderDetails.getWarehouseId());
            storageInProcessOptFormVO.setWarehouseName(warehouseById.getName());
            storageInProcessOptFormVO.setWarehousePhone(warehouseById.getPhone());
            storageInProcessOptFormVO.setWarehouseAddress(warehouseById.getAddress());
        }

        storageInProcessOptFormVO.setMainOrderNo(storageInputOrder.getMainOrderNo());
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageInProcessOptFormVO.getMainOrderNo()));
        storageInProcessOptFormVO.assemblyMainOrderData(result.getData());
        storageInProcessOptFormVO.setOrderId(storageInputOrder.getId());
        storageInProcessOptFormVO.setOrderNo(storageInputOrder.getOrderNo());
        storageInProcessOptFormVO.setPlateNumber(storageInputOrder.getPlateNumber());
        storageInProcessOptFormVO.setWarehouseNumber(storageInputOrder.getWarehouseNumber());
        storageInProcessOptFormVO.setCreateTime(storageInputOrder.getCreateTime());

        return CommonResult.success(storageInProcessOptFormVO);
    }

    /**
     * ?????????????????????
     * @param map
     * @return
     */
    @ApiOperation(value = "?????????????????????")
    @PostMapping(value = "/getWarehousingBatchNumber")
    public CommonResult getWarehousingBatchNumber(@RequestBody Map<String,Object> map){
        String orderNo = MapUtil.getStr(map, "orderNo");
//        boolean b = redisUtils.hasKey(orderNo);
//        if(b){
//            return CommonResult.success(redisUtils.get(orderNo));
//        }else{
//            //?????????????????????
//            String batchNumber = (String)omsClient.getWarehouseNumber("A").getData();
//            redisUtils.set(orderNo,batchNumber);
//            return CommonResult.success(batchNumber);
//        }
        //?????????????????????
        String batchNumber = (String)omsClient.getWarehouseNumber("A").getData();
        //redisUtils.set(orderNo,batchNumber);
        return CommonResult.success(batchNumber);

    }

    @ApiOperation(value = "??????????????????????????????????????????")
    @PostMapping(value = "/isSubmit")
    public CommonResult isSubmit(@RequestBody Map<String,Object> map) {
        String orderId = MapUtil.getStr(map, "orderId");
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(orderId);
        if(storageInputOrder.getIsSubmit().equals(1)){
            return CommonResult.success(false);
        }
        return CommonResult.success(true);
    }

    @ApiOperation(value = "??????????????????????????????")
    @PostMapping(value = "/isWarehousing")
    public CommonResult isWarehousing(@RequestBody StorageInProcessOptForm form) {
        //????????????????????????????????????
        Integer totalNumber = 0;
        Integer totalNumber1 = 0;
        if(form.getInGoodsOperationRecords().size()<=0){
            return CommonResult.error(443,"?????????????????????");
        }
        if(CollectionUtils.isNotEmpty(form.getInGoodsOperationRecords())){
            for (InGoodsOperationRecordForm inGoodsOperationRecord : form.getInGoodsOperationRecords()) {
                totalNumber = totalNumber + inGoodsOperationRecord.getNumber();
                List<GoodsLocationRecordForm> goodsLocationRecordForms = inGoodsOperationRecord.getGoodsLocationRecordForms();
                if(CollectionUtils.isNotEmpty(goodsLocationRecordForms)){
                    for (GoodsLocationRecordForm goodsLocationRecordForm : goodsLocationRecordForms) {
                        if(goodsLocationRecordForm.getNumber() != null && goodsLocationRecordForm.getKuCode() != null){
                            totalNumber1 = totalNumber1 + goodsLocationRecordForm.getNumber();
                        }
                    }
                }

            }
        }

        if(totalNumber.equals(totalNumber1)){
            return CommonResult.success();
        }
        return CommonResult.error(443,"?????????????????????");
    }


    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody StorageInCargoRejected storageInCargoRejected) {
        //??????????????????
        StorageInputOrder tmp = this.storageInputOrderService.getById(storageInCargoRejected.getStorageInOrderId());
        //????????????????????????
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getInStorageOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "??????????????????,????????????????????????");
        }
        //??????????????????
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(storageInCargoRejected.getStorageInOrderId());
        auditInfoForm.setExtDesc(SqlConstant.STORAGE_INPUT_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(storageInCargoRejected.getCause());

        switch (orderStatusEnum) {
            case CCI_1_1:
                //????????????
                this.storageInputOrderService.orderReceiving(tmp, auditInfoForm, storageInCargoRejected);
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "?????????????????????")
    @PostMapping(value = "/getQRCodeInformation")
    public CommonResult getQRCodeInformation(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        List<InGoodsOperationRecord> listByWarehousingBatchNo = inGoodsOperationRecordService.getListByWarehousingBatchNo(warehousingBatchNo);
        Integer totalAmount = 0;
        Integer totalJAmount = 0;
        Integer totalPCS = 0;

        for (InGoodsOperationRecord inGoodsOperationRecord : listByWarehousingBatchNo) {
            totalAmount = totalAmount + inGoodsOperationRecord.getNumber();
            totalJAmount = totalJAmount + inGoodsOperationRecord.getBoardNumber();
            totalPCS = totalPCS + inGoodsOperationRecord.getPcs();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(totalJAmount).append("???").append("/")
                .append(totalAmount).append("???").append("/")
                .append(totalPCS).append("PCS").append("/");

        QRCodeInformationVO qrCodeInformationVO = new QRCodeInformationVO();
        qrCodeInformationVO.setGoodInfo(stringBuffer.toString());
        qrCodeInformationVO.setWarehousingBatchNo(warehousingBatchNo);
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(listByWarehousingBatchNo.get(0).getOrderId());
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageInputOrder.getMainOrderNo()));
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(result.getData()));
        qrCodeInformationVO.setCustomerName(mainOrders.getJSONObject(0).getStr("customerName"));
        qrCodeInformationVO.setCreateTime(listByWarehousingBatchNo.get(0).getCreateTime());
        qrCodeInformationVO.setWarehouseNumber(storageInputOrder.getWarehouseNumber());
        qrCodeInformationVO.setLegalName(storageInputOrder.getLegalName());
        return CommonResult.success(qrCodeInformationVO);
    }

    @ApiOperation(value = "?????????????????????")
    @PostMapping(value = "/getHeaderInformation")
    public CommonResult getHeaderInformation(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        List<InGoodsOperationRecord> listByWarehousingBatchNo = inGoodsOperationRecordService.getListByWarehousingBatchNo(warehousingBatchNo);
        Integer totalAmount = 0;
        Integer totalJAmount = 0;
        Integer totalPCS = 0;

        for (InGoodsOperationRecord inGoodsOperationRecord : listByWarehousingBatchNo) {
            totalAmount = totalAmount + (inGoodsOperationRecord.getNumber()==null ? 0:inGoodsOperationRecord.getNumber());
            totalJAmount = totalJAmount + (inGoodsOperationRecord.getBoardNumber()==null ? 0:inGoodsOperationRecord.getBoardNumber());
            totalPCS = totalPCS + (inGoodsOperationRecord.getPcs()==null ? 0:inGoodsOperationRecord.getPcs());
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(totalJAmount).append("???").append("/")
                .append(totalAmount).append("???").append("/")
                .append(totalPCS).append("PCS").append("/");

        QRCodeInformationVO qrCodeInformationVO = new QRCodeInformationVO();
        qrCodeInformationVO.setGoodInfo(stringBuffer.toString());
        qrCodeInformationVO.setWarehousingBatchNo(warehousingBatchNo);
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(listByWarehousingBatchNo.get(0).getOrderId());
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageInputOrder.getMainOrderNo()));
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(result.getData()));
        qrCodeInformationVO.setCustomerName(mainOrders.getJSONObject(0).getStr("customerName"));
        qrCodeInformationVO.setCreateTime(listByWarehousingBatchNo.get(0).getCreateTime());
        qrCodeInformationVO.setWarehouseNumber(storageInputOrder.getWarehouseNumber());
        qrCodeInformationVO.setLegalName(storageInputOrder.getLegalName());
        qrCodeInformationVO.setGoodInfos(listByWarehousingBatchNo);
        qrCodeInformationVO.setTotalAmount(totalAmount);
        qrCodeInformationVO.setTotalJAmount(totalJAmount);
        qrCodeInformationVO.setTotalPCS(totalPCS);

        return CommonResult.success(qrCodeInformationVO);
    }

    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping(value = "/getWarehousingBatch")
    public CommonResult getWarehousingBatch(@RequestBody Map<String,Object> map) {
        Long orderId = MapUtil.getLong(map, "orderId");
        List<String> strings = inGoodsOperationRecordService.getWarehousingBatch(orderId);
        if(strings.size()<=0){
            return CommonResult.error(443,"???????????????????????????????????????????????????");
        }
        return CommonResult.success(strings);
    }

    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/findOrderRecordByPage")
    public CommonResult findOrderRecordByPage(@RequestBody QueryInOrderForm form){
        //????????????????????????
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
        IPage<StorageInputOrderNumberVO> page = this.storageInputOrderService.findOrderRecordByPage(form);
        List<String> mainOrders = new ArrayList<>();
        for (StorageInputOrderNumberVO record : page.getRecords()) {
            mainOrders.add(record.getMainOrderNo());
        }
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrders);

        for (StorageInputOrderNumberVO record : page.getRecords()) {
            record.assemblyMainOrderData(result.getData());
            List<InGoodsOperationRecord> listByOrderId = inGoodsOperationRecordService.getListByOrderId(record.getId(), record.getOrderNo());
            Integer number = 0;
            Integer pcs = 0;
            Integer nowNumber = 0;
            Integer nowPcs = 0;
            for (InGoodsOperationRecord inGoodsOperationRecord : listByOrderId) {
                if(inGoodsOperationRecord.getNumber() != null){
                    number = number + inGoodsOperationRecord.getNumber();
                }
                if(inGoodsOperationRecord.getPcs() != null){
                    pcs = pcs + inGoodsOperationRecord.getPcs();
                    nowPcs = nowPcs + inGoodsOperationRecord.getPcs();
                }
                List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(inGoodsOperationRecord.getId());
                for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecordByGoodId) {
                    if(goodsLocationRecord.getUnDeliveredQuantity() != null){
                        nowNumber = nowNumber + goodsLocationRecord.getUnDeliveredQuantity();
                    }

                }
            }
            record.setOrderQuantity(number);
            record.setOrderQuantityPcs(pcs);
            record.setNowOrderQuantity(nowNumber);
            record.setNowOrderQuantityPcs(nowPcs);
        }
        return CommonResult.success(page);
    }


    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/viewOrderRecords")
    public CommonResult viewOrderRecords(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        //????????????????????????
        List<InGoodsOperationRecord> listByOrderId = inGoodsOperationRecordService.getListByOrderId(id);
        List<String> skuList = new ArrayList<>();
        List<String> warehousingBatchNos = new ArrayList<>();
        List<InGoodsOperationRecordOrderVO> inGoodsOperationRecordOrderVOS = new ArrayList<>();
        for (InGoodsOperationRecord inGoodsOperationRecord : listByOrderId) {
            Integer nowNumber = 0;
            Integer nowPcs = 0;
            if(inGoodsOperationRecord.getPcs() != null){
                nowPcs = nowPcs + inGoodsOperationRecord.getPcs();
            }
            List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(inGoodsOperationRecord.getId());
            for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecordByGoodId) {
                nowNumber = nowNumber + goodsLocationRecord.getUnDeliveredQuantity();
            }
            InGoodsOperationRecordOrderVO convert = ConvertUtil.convert(inGoodsOperationRecord, InGoodsOperationRecordOrderVO.class);
            convert.setNowNumber(nowNumber);
            convert.setNowPcs(nowPcs);
            convert.setStorageTime(this.getStorageTime(convert.getCreateTime().toString().replace("T"," "), LocalDateTime.now().toString().replace("T"," ")));
            inGoodsOperationRecordOrderVOS.add(convert);
            skuList.add(inGoodsOperationRecord.getSku());
            warehousingBatchNos.add(inGoodsOperationRecord.getWarehousingBatchNo());
        }
        skuList = removeDuplicate(skuList);
        warehousingBatchNos = removeDuplicate(warehousingBatchNos);
        //????????????
        List<OrderOutRecord> orderOutRecords = warehouseGoodsService.getListBySkuAndBatchNo(skuList,warehousingBatchNos);
        Map<String,Object> map1 = new HashMap();
        map1.put("in",inGoodsOperationRecordOrderVOS);
        map1.put("out",orderOutRecords);
        return CommonResult.success(map1);
    }

    //list????????????
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    //??????????????????
    public String getStorageTime(String startTime,String endTime){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        try {
            one = df.parse(startTime);
            two = df.parse(endTime);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day + "???" + hour + "??????" ;
    }


    //???????????????
    @Value("${address.storageAddr}")
    private String filepath;

    @ApiOperation(value = "???????????????")
    @GetMapping(value = "/exportInWarehouseReceipt")
    public void exportInWarehouseReceipt(@RequestParam("orderId") Long orderId, HttpServletResponse response){
        Map map = new HashMap();
        map.put("orderId",orderId);
        StorageInProcessOptFormVO storageInProcessOptFormVO = (StorageInProcessOptFormVO)this.getDataInConfirmEntry(map).getData();


        File file = new File(filepath);
        String name = file.getName();

        try {
            InputStream inputStream = new FileInputStream(file);
            Workbook templateWorkbook = null;
            String fileType = name.substring(name.lastIndexOf("."));
            if (".xls".equals(fileType)) {
                templateWorkbook = new HSSFWorkbook(inputStream); // 2003-
            } else if (".xlsx".equals(fileType)) {
                templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
            } else {

            }
            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = "?????????";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();

//            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            //?????????????????????
            String number = null;
            Integer sNumber = 0;
            Integer borderNumber = 0;
            Integer pcs = 0;
            Double weight = 0.0;
            Double volume = 0.0;
            if(CollectionUtils.isNotEmpty(storageInProcessOptFormVO.getInGoodsOperationRecords())){
                List<InGoodsOperationRecordVO> inGoodsOperationRecords = storageInProcessOptFormVO.getInGoodsOperationRecords();
                for (InGoodsOperationRecordVO inGoodsOperationRecord : inGoodsOperationRecords) {
                    if(inGoodsOperationRecord.getBoardNumber() == null){
                        inGoodsOperationRecord.setBoardNumber(0);
                    }
                    if(inGoodsOperationRecord.getPcs() == null){
                        inGoodsOperationRecord.setPcs(0);
                    }
                    if(inGoodsOperationRecord.getPcs() != null){
                        pcs = pcs + inGoodsOperationRecord.getPcs();
                    }
                    if(inGoodsOperationRecord.getBoardNumber() != null){
                        borderNumber = borderNumber + inGoodsOperationRecord.getBoardNumber();
                    }
                    if(inGoodsOperationRecord.getNumber() != null){
                        sNumber = sNumber + inGoodsOperationRecord.getNumber();
                    }
                    if(inGoodsOperationRecord.getWeight() != null){
                        weight = weight + inGoodsOperationRecord.getWeight();
                    }
                    if(inGoodsOperationRecord.getVolume() != null){
                        volume = volume + inGoodsOperationRecord.getVolume();
                    }
                }
                for (int i = 0; i < inGoodsOperationRecords.size(); i++) {
                    for (int j = i+1; j < inGoodsOperationRecords.size(); j++) {
                        if(inGoodsOperationRecords.get(i).getSku().equals(inGoodsOperationRecords.get(j).getSku())){
                            InGoodsOperationRecordVO inGoodsOperationRecordVO = inGoodsOperationRecords.get(i);
                            inGoodsOperationRecordVO.setNumber((inGoodsOperationRecordVO.getNumber()==null ? 0: inGoodsOperationRecordVO.getNumber())
                                    + (inGoodsOperationRecords.get(j).getNumber()==null ? 0: inGoodsOperationRecords.get(j).getNumber()));
                            inGoodsOperationRecordVO.setBoardNumber((inGoodsOperationRecordVO.getBoardNumber()==null ? 0: inGoodsOperationRecordVO.getBoardNumber())
                                    + (inGoodsOperationRecords.get(j).getBoardNumber()==null ? 0: inGoodsOperationRecords.get(j).getBoardNumber()));
                            inGoodsOperationRecordVO.setPcs((inGoodsOperationRecordVO.getPcs()==null ? 0: inGoodsOperationRecordVO.getPcs())
                                    + (inGoodsOperationRecords.get(j).getPcs()==null ? 0: inGoodsOperationRecords.get(j).getPcs()));
                            inGoodsOperationRecordVO.setWeight((inGoodsOperationRecordVO.getWeight()==null ? 0: inGoodsOperationRecordVO.getWeight())
                                    + (inGoodsOperationRecords.get(j).getWeight()==null ? 0: inGoodsOperationRecords.get(j).getWeight()));
                            inGoodsOperationRecordVO.setVolume((inGoodsOperationRecordVO.getVolume()==null ? 0: inGoodsOperationRecordVO.getVolume())
                                    + (inGoodsOperationRecords.get(j).getVolume()==null ? 0: inGoodsOperationRecords.get(j).getVolume()));
                            inGoodsOperationRecordVO.setRemarks(inGoodsOperationRecordVO.getRemarks()+inGoodsOperationRecords.get(j).getRemarks());
                            inGoodsOperationRecords.remove(j);
                        }
                    }
                }
                excelWriter.fill(new FillWrapper("goodList", inGoodsOperationRecords), writeSheet);

                number = borderNumber +"???"+ sNumber + "???" + pcs + "pcs";
            }
            //?????????????????????
            Map<String,Object> dataMap = new HashMap<String, Object>();

            List<Long> operationId = storageInProcessOptFormVO.getOperationId();
            List<Long> cardTypeId = storageInProcessOptFormVO.getCardTypeId();


            List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("operation").getData();
            List<InitComboxSVO> data1 = omsClient.initDictNameByDictTypeCode("cardType").getData();
            for (InitComboxSVO datum : data) {
                for (Long aLong : operationId) {
                    if(datum.getId().equals(aLong)){
                        dataMap.put(datum.getValue(), "???");
                    }
                }
            }

            for (InitComboxSVO datum : data1) {
                for (Long aLong : cardTypeId) {
                    if(datum.getId().equals(aLong)){
                        dataMap.put(datum.getValue(), "???");
                    }
                }
            }

//            dataMap.put("poundWeight", "1");
//            dataMap.put("labeling ", "1");
//            dataMap.put("photograph ","1");
//            dataMap.put("measureSize", "1");
//            dataMap.put("numberOfPoints", "1");
//            dataMap.put("selfUnloading", "1");
//            dataMap.put("warehouseUnloading", "1");
//            dataMap.put("compositeBoard", "1");
//            dataMap.put("rubberSheet", "1");
//            dataMap.put("board", "1");
//            dataMap.put("cardboard", "1");
//            dataMap.put("woodenCase", "1");
            if(storageInProcessOptFormVO.getYes() != null && storageInProcessOptFormVO.getYes().equals(true)){
                dataMap.put("yes", "???");
            }
            if(storageInProcessOptFormVO.getNo() != null && storageInProcessOptFormVO.getNo().equals(true)){
                dataMap.put("no", "???");
            }
            if(storageInProcessOptFormVO.getIsGone() != null && storageInProcessOptFormVO.getIsGone().equals(true)){
                dataMap.put("isGone", "???");
            }
            if(storageInProcessOptFormVO.getIsInstructions() != null && storageInProcessOptFormVO.getIsInstructions().equals(true)){
                dataMap.put("isInstructions", "???");
            }
            if(storageInProcessOptFormVO.getIsDoorCollection() != null && storageInProcessOptFormVO.getIsDoorCollection().equals(true)){
                dataMap.put("isDoorCollection", "???");
            }
            if(storageInProcessOptFormVO.getIsSelfDelivery() != null && storageInProcessOptFormVO.getIsSelfDelivery().equals(true)){
                dataMap.put("isSelfDelivery", "???");
            }
            if(storageInProcessOptFormVO.getIsGoldLabels() != null && storageInProcessOptFormVO.getIsGoldLabels().equals(true)){
                dataMap.put("isGoldLabels", "???");
            }
            if(storageInProcessOptFormVO.getIsImproperPacking() != null && storageInProcessOptFormVO.getIsImproperPacking().equals(true)){
                dataMap.put("isImproperPacking", "???");
            }
            if(storageInProcessOptFormVO.getIsTomOpen() != null && storageInProcessOptFormVO.getIsTomOpen().equals(true)){
                dataMap.put("isTomOpen", "???");
            }
            if(storageInProcessOptFormVO.getIsReTaped() != null && storageInProcessOptFormVO.getIsReTaped().equals(true)){
                dataMap.put("isReTaped", "???");
            }
            if(storageInProcessOptFormVO.getIsCrushedCollapsed() != null && storageInProcessOptFormVO.getIsCrushedCollapsed().equals(true)){
                dataMap.put("isCrushedCollapsed", "???");
            }
            if(storageInProcessOptFormVO.getIsWaterGreased() != null && storageInProcessOptFormVO.getIsWaterGreased().equals(true)){
                dataMap.put("isWaterGreased", "???");
            }
            if(storageInProcessOptFormVO.getIsPuncturedHoles() != null && storageInProcessOptFormVO.getIsPuncturedHoles().equals(true)){
                dataMap.put("isPuncturedHoles", "???");
            }
            if(storageInProcessOptFormVO.getIsDamagedCtn() != null && storageInProcessOptFormVO.getIsDamagedCtn().equals(true)){
                dataMap.put("isDamagedCtn", "???");
            }

            dataMap.put("num1", storageInProcessOptFormVO.getNum1());
            dataMap.put("num2", storageInProcessOptFormVO.getNum2());

            dataMap.put("tomOpenNumber", storageInProcessOptFormVO.getTomOpenNumber());

            dataMap.put("reTapedNumber", storageInProcessOptFormVO.getReTapedNumber());

            dataMap.put("crushedCollapsedNumber", storageInProcessOptFormVO.getCrushedCollapsedNumber());

            dataMap.put("waterGreasedNumber", storageInProcessOptFormVO.getWaterGreasedNumber());

            dataMap.put("puncturedHolesNumber", storageInProcessOptFormVO.getPuncturedHolesNumber());

            dataMap.put("damagedCtnNumber", storageInProcessOptFormVO.getDamagedCtnNumber());
            dataMap.put("remarks",storageInProcessOptFormVO.getRemarks());
            dataMap.put("marks",storageInProcessOptFormVO.getMarks());
            dataMap.put("documents",storageInProcessOptFormVO.getDocuments());
            dataMap.put("warehouseManagement",storageInProcessOptFormVO.getWarehouseManagement());
            dataMap.put("driver",storageInProcessOptFormVO.getDriver());
            dataMap.put("beizhu",storageInProcessOptFormVO.getBeizhu());

            dataMap.put("warehouseNumber", storageInProcessOptFormVO.getWarehouseNumber());
            dataMap.put("createTime", storageInProcessOptFormVO.getCreateTime().toString().replace("T"," "));
            dataMap.put("plateNumber", storageInProcessOptFormVO.getPlateNumber());
            dataMap.put("customerName", storageInProcessOptFormVO.getCustomerName());
            dataMap.put("warehouseName",storageInProcessOptFormVO.getWarehouseName());
            dataMap.put("warehousePhone",storageInProcessOptFormVO.getWarehousePhone());
            dataMap.put("warehouseAddress",storageInProcessOptFormVO.getWarehouseAddress());
            dataMap.put("number",number);
            dataMap.put("weight",weight);
            dataMap.put("volume",volume);

            excelWriter.fill(dataMap, writeSheet);

            excelWriter.finish();
            outStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}

