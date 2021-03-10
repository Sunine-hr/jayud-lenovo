package com.jayud.oceanship.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.*;
import com.jayud.oceanship.feign.FileClient;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.OrderFlowSheet;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.po.SeaPort;
import com.jayud.oceanship.service.IOrderFlowSheetService;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.service.ISeaPortService;
import com.jayud.oceanship.vo.GoodsVO;
import com.jayud.oceanship.vo.OrderAddressVO;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import com.jayud.oceanship.vo.SeaOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

import static com.jayud.common.enums.OrderStatusEnum.SEA_S_2;

/**
 * <p>
 * 海运订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@RestController
@Slf4j
@Api(tags = "海运订单")
@RequestMapping("/seaOrder")
public class SeaOrderController {

    @Autowired
    private ISeaOrderService seaOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private IOrderFlowSheetService orderFlowSheetService;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ISeaPortService seaPortService;

    @ApiOperation("分页查询海运订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QuerySeaOrderForm form){

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
        Class<SeaOrderFormVO> seaOrderFormVOClass = SeaOrderFormVO.class;
        Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if(annotation!=null){
                Map map = new HashMap<>();
                map.put("key",declaredField.getName());
                map.put("name",annotation.value());
                list.add(map);
            }
        }
        Map map1 = new HashMap();
        map1.put("header",list);
        IPage<SeaOrderFormVO> page = this.seaOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo",new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<SeaOrderFormVO> records = page.getRecords();
        List<Long> seaOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        for (SeaOrderFormVO record : records) {
            seaOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
            if(record.getSeaBookShipForm().getAgentSupplierId()!=null){
                supplierIds.add(record.getSeaBookShipForm().getAgentSupplierId());
            }

            record.getSeaBookShipForm().getFile(prePath);
        }

        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(seaOrderIds, BusinessTypeEnum.HY.getCode()).getData();

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size()>0) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //获取发货人信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(seaOrderIds, BusinessTypeEnum.HY.getCode() );
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}", seaOrderIds);
        }

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (SeaOrderFormVO record : records) {
            //组装商品信息
            List<GoodsVO> goodsVOS = new ArrayList<>();
            for (GoodsVO goodsVO : goods) {
                if (record.getId().equals(goodsVO.getBusinessId())
                        && BusinessTypeEnum.HY.getCode().equals(goodsVO.getBusinessType())) {
                    goodsVOS.add(goodsVO);
                }
            }
            record.setGoodsVOS(goodsVOS);
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            //处理地址信息
            for (OrderAddressVO address : resultOne.getData()) {
                address.getFile(prePath);
                if(address.getOrderNo().equals(record.getOrderNo())){
                    record.processingAddress(address);
                }
            }
        }

        //获取目的港名称
        List<SeaPort> seaPorts = seaPortService.list();
        List<SeaOrderFormVO> records1 = page.getRecords();
        for (SeaOrderFormVO seaOrderFormVO : records1) {
            //查询贸易方式
            for (SeaPort seaPort : seaPorts) {
                if (seaPort.getCode().equals(seaOrderFormVO.getPortDepartureCode())){
                    seaOrderFormVO.setPortDepartureName(seaPort.getName());
                }
                if(seaPort.getCode().equals(seaOrderFormVO.getPortDestinationCode())){
                    seaOrderFormVO.setPortDestinationName(seaPort.getName());
                }
                if(seaPort.getCode().equals(seaOrderFormVO.getTransitPortCode())){
                    seaOrderFormVO.setTransitPort(seaPort.getName());
                }
            }
        }
        page.setRecords(records1);
        map1.put("pageInfo",new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    //操作指令,cmd = S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收
    @ApiOperation(value = "执行海运流程操作")
    @PostMapping(value = "/doSeaProcessOpt")
    public CommonResult doSeaProcessOpt(@RequestBody @Valid SeaProcessOptForm form , BindingResult result) {

        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444,error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主订单id/海运订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //海运订单信息
        SeaOrder seaOrder = this.seaOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(seaOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(seaOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        if(!seaOrder.getStatus().equals(form.getStatus())){
            return CommonResult.error(400, "当前订单正在操作");
        }
//        OrderStatusEnum statusEnum = OrderStatusEnum.getSeaOrderNextStatus(seaOrder.getStatus());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("f_status",seaOrder.getStatus());
        queryWrapper.eq("order_no",seaOrder.getOrderNo());
        OrderFlowSheet orderFlowSheet = orderFlowSheetService.getOne(queryWrapper);
        OrderStatusEnum statusEnum = OrderStatusEnum.getSeaOrderNextStatus(orderFlowSheet.getStatus());
        if (statusEnum == null) {
            log.error("执行海运流程操作失败,超出流程之外 data={}", seaOrder.toString());
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //校验参数
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //指令操作
        switch (statusEnum) {
            case SEA_S_1: //海运接单
                SeaOrder seaOrder1 = new SeaOrder();
                seaOrder1.setOrderTaker(form.getOperatorUser());
                seaOrder1.setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                this.seaOrderService.updateProcessStatus(seaOrder1 , form);
                break;
            case SEA_S_2: //订船
                this.seaOrderService.doSeaBookShipOpt(form);
                break;
            case SEA_S_3: //确认订单入仓
                this.seaOrderService.updateProcessStatus(new SeaOrder(), form);
                break;
            case SEA_S_4: //提交补料
                this.seaOrderService.updateOrSaveProcessStatus(form);
                break;
            case SEA_S_5: //确认草稿提单
            case SEA_S_6: //确认装船
            case SEA_S_7: //确认放单
            case SEA_S_8: //确认到港
                this.seaOrderService.doSeaBookShipOpt(form);
                break;
            case SEA_S_9: //海外代理
                StringBuilder sb = new StringBuilder();
                form.getProxyServiceType().forEach(e -> sb.append(e).append(","));
                String proxyServiceType = sb.length() == 0 ? null : sb.substring(0, sb.length() - 1);
                SeaOrder seaOrder2 = new SeaOrder();
                seaOrder2.setOverseasSuppliersId(form.getAgentSupplierId());
                seaOrder2.setProxyServiceType(proxyServiceType);
                this.seaOrderService.updateProcessStatus(seaOrder2, form);
                break;
            case SEA_S_10: //确认签收
                this.seaOrderService.updateProcessStatus(new SeaOrder(), form);
                break;
        }

        return CommonResult.success();
    }


    @ApiOperation(value = "查询订单详情 seaOrderId=海运订单id")
    @PostMapping(value = "/getSeaOrderDetails")
    public CommonResult<SeaOrderVO> getAirOrderDetails(@RequestBody Map<String, Object> map) {
        Long seaOrderId = MapUtil.getLong(map, "seaOrderId");
        if (seaOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        SeaOrderVO airOrderDetails = this.seaOrderService.getSeaOrderDetails(seaOrderId);

        return CommonResult.success(airOrderDetails);
    }


    @ApiOperation(value = "订船驳回编辑 id=海运订单id")
    @PostMapping(value = "/bookShipRejectionEdit")
    public CommonResult bookingRejectionEdit(@RequestBody SeaProcessOptForm form) {
        if (form.getMainOrderId() == null
                || form.getOrderId() == null
                || form.getSeaBookShipForm().getId() == null) {
            log.warn("海运订单编号/海运订单id必填/海运订船id必填 data={}", JSONUtil.toJsonStr(form));
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        SeaOrder seaOrder = this.seaOrderService.getById(form.getOrderId());
        if (!OrderStatusEnum.SEA_S_3_2.getCode().equals(seaOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(seaOrder.getStatus()));
            return CommonResult.error(400, "当前订单状态无法进行操作");
        }
        form.setStatus(SEA_S_2.getCode());
        //校验参数
        form.checkProcessOpt(SEA_S_2);
        //执行订船驳回编辑
        this.seaOrderService.doSeaBookShipOpt(form);
        return CommonResult.success();
    }



    @ApiOperation(value = "海运订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody SeaCargoRejected seaCargoRejected) {
        //查询空运订单
        SeaOrder tmp = this.seaOrderService.getById(seaCargoRejected.getSeaOrderId());
        //获取相应驳回操作
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getSeaOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(seaCargoRejected.getSeaOrderId());
        auditInfoForm.setExtDesc(SqlConstant.SEA_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(seaCargoRejected.getCause());

        Integer rejectOptions = seaCargoRejected.getRejectOptions() == null ? 1 : seaCargoRejected.getRejectOptions();
        seaCargoRejected.setRejectOptions(rejectOptions);
        switch (orderStatusEnum) {
            case SEA_S_1_1:
                //订单驳回
                this.seaOrderService.orderReceiving(tmp, auditInfoForm, seaCargoRejected);
                break;
            case SEA_S_2_1:
            case SEA_S_3_1:
                this.seaOrderService.rejectedOpt(tmp, auditInfoForm, seaCargoRejected);
                break;
        }

        return CommonResult.success();
    }

    /**
     * 导出补料单
     */
    @Value("${address.seaAddr}")
    private String filePath;

    @ApiOperation(value = "导出补料单")
    @GetMapping(value = "/uploadExcel")
    public void uploadExcel(@RequestParam("orderId") Long orderId, HttpServletResponse response) {
//        Long orderId = MapUtil.getLong(map1, "OrderId");
        SeaOrderVO seaOrderDetails = seaOrderService.getSeaOrderDetails(orderId);


//        ClassPathResource classPathResource = new ClassPathResource("/static/海运.xlsx");
//        String filename1 = classPathResource.getFilename();

        File file = new File(filePath);
        String filename1 = file.getName();

        try {
//            InputStream inputStream = classPathResource.getInputStream();
            InputStream inputStream = new FileInputStream(file);
            Workbook templateWorkbook = null;
            String fileType = filename1.substring(filename1.lastIndexOf("."));
            if (".xls".equals(fileType)) {
                templateWorkbook = new HSSFWorkbook(inputStream); // 2003-
            } else if (".xlsx".equals(fileType)) {
                templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
            }else{

            }
            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = "海运补料";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename+".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            //将集合数据填充
            excelWriter.fill(new FillWrapper("delivery",seaOrderDetails.getDeliveryAddress()),fillConfig,writeSheet);
            excelWriter.fill(new FillWrapper("shipping",seaOrderDetails.getShippingAddress()),fillConfig,writeSheet);
            if(seaOrderDetails.getNotificationAddress()!=null && seaOrderDetails.getNotificationAddress().size()>0){
                excelWriter.fill(new FillWrapper("notification",seaOrderDetails.getNotificationAddress()),fillConfig,writeSheet);
            }
            excelWriter.fill(new FillWrapper("goodone",seaOrderDetails.getGoodsForms()),fillConfig,writeSheet);
            excelWriter.fill(new FillWrapper("goodtwo",seaOrderDetails.getGoodsForms()),writeSheet);

            //将指定数据填充
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shipCompany", seaOrderDetails.getSeaBookshipVO().getShipCompany());
            map.put("shipNumber", seaOrderDetails.getSeaBookshipVO().getShipNumber());
            map.put("portDeparture", seaOrderDetails.getPortDeparture());
            map.put("portDestination", seaOrderDetails.getPortDestination());
            map.put("cabinetNumber", seaOrderDetails.getCabinetNumber());
            map.put("paperStripSeal", seaOrderDetails.getPaperStripSeal());
            map.put("cabinetSize", seaOrderDetails.getCabinetSizeName());
            map.put("cabinetType", seaOrderDetails.getCabinetTypeName());
            if(seaOrderDetails.getCabinetTypeName().equals("FCL")){
                map.put("whether","√");
            }else{
                map.put("whether2","√");
            }

            List<GoodsVO> goodsForms = seaOrderDetails.getGoodsForms();
            Integer totalBulkCargoAmount = 0;
            Double totalWeights = 0.0;
            Double totalvolume = 0.0;
            for (GoodsVO goodsForm : goodsForms) {
                totalBulkCargoAmount = totalBulkCargoAmount + goodsForm.getBulkCargoAmount();
                totalWeights = totalWeights + goodsForm.getTotalWeight();
                if(goodsForm.getVolume()!=null){
                    totalvolume = totalvolume + goodsForm.getVolume();
                }

            }
            map.put("totalBulkCargoAmount", totalBulkCargoAmount);
            map.put("totalWeights", totalWeights);
            map.put("totalvolume", totalvolume);
            excelWriter.fill(map, writeSheet);

            excelWriter.finish();
            outStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

