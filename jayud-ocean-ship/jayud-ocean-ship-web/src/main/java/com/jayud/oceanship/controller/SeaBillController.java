package com.jayud.oceanship.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.AddSeaBillForm;
import com.jayud.oceanship.bo.QueryBulkCargolForm;
import com.jayud.oceanship.bo.QuerySeaBillForm;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.feign.FileClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.SeaBill;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.service.ISeaBillService;
import com.jayud.oceanship.service.ISeaContainerInformationService;
import com.jayud.oceanship.service.ISeaOrderService;
import com.jayud.oceanship.service.ISeaPortService;
import com.jayud.oceanship.utils.DateUtil;
import com.jayud.oceanship.utils.NumUtil;
import com.jayud.oceanship.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jayud.oceanship.utils.PdfUtil.createPdfStream;

/**
 * <p>
 * 提单信息表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-06-23
 */
@RestController
@Api("提单管理")
@RequestMapping("/seaBill")
public class SeaBillController {

    @Autowired
    private ISeaBillService seaBillService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ISeaOrderService seaOrderService;

    @Autowired
    private ISeaContainerInformationService seaContainerInformationService;

    @Autowired
    private ISeaPortService seaPortService;

    @ApiOperation("分页查询提单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QuerySeaBillForm form) {
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
        Class<SeaBillFormVO> replenishmentFormVOClass = SeaBillFormVO.class;
        Field[] declaredFields = replenishmentFormVOClass.getDeclaredFields();
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

        IPage<SeaBillFormVO> page = this.seaBillService.findBillByPage(form);

        //IPage<SeaOrderFormVO> page = this.seaOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        List<SeaBillFormVO> records = page.getRecords();
//        List<String> seaOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        for (SeaBillFormVO record : records) {
//            seaOrderIds.add(record.getOrderNo());
            //获取海运订单信息
            SeaOrderVO seaOrderByOrderNO = seaOrderService.getSeaOrderByOrderNO(record.getSeaOrderId());
            mainOrder.add(seaOrderByOrderNO.getMainOrderNo());
            record.setMainOrderNo(seaOrderByOrderNO.getMainOrderNo());
            record.setStatus(seaOrderByOrderNO.getStatus());
            record.setProcessStatus(seaOrderByOrderNO.getProcessStatus());
        }


        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (SeaBillFormVO record : records) {

            record.setOrderId(record.getSeaOrderId());
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //处理地址信息

            //获取柜型数量
//            if (record.getCabinetType().equals(1)) {
//                List<CabinetSizeNumberVO> cabinetSizeNumberVOS = cabinetSizeNumberService.getList(record.getId());
//                record.setCabinetSizeNumbers(cabinetSizeNumberVOS);
//                record.assemblyCabinetInfo(cabinetSizeNumberVOS);
//            }
            //获取货柜信息
            if (record.getCabinetType().equals(1)) {
                List<SeaContainerInformationVO> seaContainerInformations = seaContainerInformationService.getList(record.getId());
                record.setSeaContainerInformations(seaContainerInformations);
            }
        }
        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }


    @ApiOperation("增加或修改提单信息")
    @PostMapping("/saveOrUpdateSeaBill")
    public CommonResult saveOrUpdateSeaBill(@RequestBody AddSeaBillForm form) {
        boolean result = seaBillService.saveOrUpdateSeaBill(form);
        if(!result){
            return CommonResult.error(444,"修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation("根据提单id获取提单信息")
    @PostMapping("/getSeaBillDetails")
    public CommonResult getSeaBillDetails(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        SeaBillVO seaBillVO = seaBillService.getSeaBillById(id);
        return CommonResult.success(seaBillVO);
    }

    @ApiOperation("根据条件获取散货提单信息")
    @PostMapping("/getBulkCargoSeaBill")
    public CommonResult getBulkCargoSeaBill(@RequestBody QueryBulkCargolForm form) {
        List<SeaBillVO> seaBillVOS = seaBillService.getSeaBillByCondition(form);
        return CommonResult.success(seaBillVOS);
    }

    @Value("${address.billAddress}")
    private String path;

    @ApiOperation("导出提单pdf")
    @GetMapping("/exportPdf")
    public void exportPdf(@RequestParam("id") Long id , @RequestParam("company") String company , HttpServletResponse response) {
        //通过id获取提单信息
        SeaBill seaBill = seaBillService.getById(id);

        Map<String, String> resultMap=new HashMap<>();
        if(seaBill.getShipperInformation() != null){
            resultMap.put("shipperInformation",seaBill.getShipperInformation());
        }
        if(seaBill.getConsigneeInformation() != null){
            resultMap.put("consigneeInformation",seaBill.getConsigneeInformation());
        }
        if(seaBill.getNotifierInformation() != null){
            resultMap.put("notifierInformation",seaBill.getNotifierInformation());
        }
        if(seaBill.getAgentInformation() != null){
            resultMap.put("agentInformation",seaBill.getAgentInformation());
        }
        if(seaBill.getPlaceOfDelivery() != null){
            resultMap.put("placeOfDelivery",seaBill.getPlaceOfDelivery());
        }
        if(seaBill.getDestination() != null){
            resultMap.put("destination",seaBill.getDestination());
        }
        if(seaBill.getPortDepartureCode() != null){
            resultMap.put("portDepartureCode",seaPortService.getPortName(seaBill.getPortDepartureCode()));
        }
        if(seaBill.getPortDestinationCode() != null){
            //通过港口代码获取代码名称
            resultMap.put("portDestinationCode",seaPortService.getPortName(seaBill.getPortDestinationCode()));
        }
        if(seaBill.getShippingMark() != null){
            resultMap.put("shippingMark",seaBill.getShippingMark());
        }
        if(seaBill.getGoodName() != null){
            resultMap.put("goodName",seaBill.getGoodName());
        }
        if(seaBill.getNumber() != null){
            resultMap.put("number",seaBill.getNumber()+seaBill.getNumberUnit());
        }
        if(seaBill.getWeight() != null){
            resultMap.put("weight",seaBill.getWeight()+"KGS");
        }
        if(seaBill.getVolume() != null){
            resultMap.put("volume",seaBill.getVolume()+"CBM");
        }
        if(seaBill.getIsFreightCollect().equals(true)){
            resultMap.put("isFreightCollect","FREIGHT COLLECT");
        }else {
            resultMap.put("isFreightCollect","FREIGHT PREPAID");
        }
        if(seaBill.getNumberOfBl() != null){
            resultMap.put("numberOfBl",seaBill.getNumberOfBl());
        }
        if(seaBill.getSailingTime() != null){

            resultMap.put("sailingTime", DateUtil.dateToEnglish(seaBill.getSailingTime()));
            resultMap.put("placeSailingTime","SEHNZHEN,"+ DateUtil.dateToEnglish(seaBill.getSailingTime()));

        }


        List<SeaContainerInformationVO> list = seaContainerInformationService.getListBySeaRepNo(seaBill.getOrderNo());
        StringBuffer str = new StringBuffer();
        if(CollectionUtils.isNotEmpty(list)){
            StringBuffer stringBuffer = new StringBuffer();
            for (SeaContainerInformationVO seaContainerInformationVO : list) {
                if(seaContainerInformationVO.getCabinetNumber() != null){
                    stringBuffer.append(seaContainerInformationVO.getCabinetNumber()).append("/")
                            .append(seaContainerInformationVO.getPaperStripSeal()).append("/")
                            .append(seaContainerInformationVO.getCabinetName()).append("/")
                            .append(seaContainerInformationVO.getPlatNumber()+seaContainerInformationVO.getPacking()).append("/")
                            .append(seaContainerInformationVO.getWeight()+"KGS").append("/")
                            .append(seaContainerInformationVO.getVolume()+"CBM").append("    ");
                }
                str.append("ONE(1) × "+seaContainerInformationVO.getCabinetName()+" ONLY").append(" ");

            }
            resultMap.put("cabinet",stringBuffer.toString());
        }
        if(seaBill.getCabinetTypeName().equals("LCL")){
            Integer number = seaBill.getNumber();

            resultMap.put("sayTotal", NumUtil.analyze(number.toString()).toUpperCase()+" "+seaBill.getNumberUnit()+" ONLY");
        }else{
            resultMap.put("sayTotal",str.toString()+" ONLY");
        }
        SeaOrder byId = seaOrderService.getById(seaBill.getSeaOrderId());
        resultMap.put("mainOrderNo",byId.getMainOrderNo());
        resultMap.put("company",company.toUpperCase());
        if(seaBill.getBillNo() != null){
            resultMap.put("billNo",seaBill.getBillNo());
        }
        if(seaBill.getVoyage() != null){
            resultMap.put("voyage",seaBill.getVoyage().toString());
        }
        if(seaBill.getShipName() != null){
            resultMap.put("shipName",seaBill.getShipName() +" "+ seaBill.getShipNumber());
        }
        if(seaBill.getTransportClause() != null){
            resultMap.put("transportClause",seaBill.getTransportClause());
        }


        //2.根据模板填充数据源
        ByteArrayOutputStream pdf = createPdfStream(path, resultMap,"佳裕达");


        //3.输出填充后的文件
        String fileName = seaBill.getBillNo() + ".pdf";
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/vnd.ms-pdf");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename );

            out.write(pdf.toByteArray());
            out.flush();
            out.close();
            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }


}

