package com.jayud.tools.controller;

import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jayud.common.CommonResult;
import com.jayud.common.exception.ApiException;
import com.jayud.tools.utils.ExcelUtil;
import com.jayud.tools.utils.RedisUtils;
import com.jayud.tools.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/wmsexcel")
@Api(tags = "T005-WMS-Excel数据导出")
@ApiSort(value = 5)
@Slf4j
public class WMSExcelController {

    /**
     * Excel 拆分
     */
    public final static String EXCELSPLIT = "EXCELSPLIT";

    public final static String EXPORTWMSDATALIST = "EXPORTWMSDATALIST";

    @Autowired
    private RedisUtils redisUtils;
    /**
     * excelList
     */
    private static List<List<Object>> excelList = Collections.synchronizedList(new ArrayList<List<Object>>());

    /**
     * 读取大数据Excel
     * @return
     */
    private RowHandler createRowHandler() {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                Object o = rowlist.get(0);
                String s = o != null ? o.toString() : null;
                //第一列，不为 null 或 "" ,加入数据
                if(o != null && !s.equals("")){
                    excelList.add(rowlist);
                }
            }
        };
    }

    @ApiOperation(value = "WMS，龙岗仓库，博亚通制单程序,excel拆分")
    @PostMapping(value = "/excelSplit")
    @ApiOperationSupport(order = 1)
    public CommonResult excelSplit(
            @RequestParam(value = "userId",required=false) Long userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response){

        userId = (userId != null) ? userId : 1;//userId,不存在，设置为1

        //准备导出的数据
        prepareData(userId, file);

        return CommonResult.success("数据上传成功，已解析拆分完毕！");
    }

    @ApiOperation(value = "WMS，测试导出")
    @GetMapping("/exportExcel")
    @ApiOperationSupport(order = 2)
    public void exportExcel(@RequestParam(value = "userId",required=false) Long userId, HttpServletRequest request, HttpServletResponse response) {
        try {
            userId = (userId != null) ? userId : 1;//userId,不存在，设置为1
            //导出的数据
            String json = redisUtils.get(EXPORTWMSDATALIST+"_"+userId);
            List<ExportWMSData> list2 = JSON.parseObject(json, new TypeReference<List<ExportWMSData>>() {});
            System.out.println(list2);

            //准备导出数据到Excel，模板填充数据
            ClassPathResource classPathResource = new ClassPathResource("template_data/model.xls");
            try {
                InputStream inputStream = classPathResource.getInputStream();

                HSSFWorkbook wb = new HSSFWorkbook(inputStream);

                HSSFSheet sheet = wb.getSheet("model");//获取 sheet

                for(int i=0; i<list2.size(); i++){
                    HSSFSheet sheet_i = wb.createSheet(String.valueOf(i+1));
                }

                //list2-测试

                ExportWMSData exportWMSData = list2.get(1);

                //在相应的单元格进行赋值

                //从0开始，(8,1)，指第9行 第2列-->备注
                HSSFCell cell_remarks = sheet.getRow(8).getCell(1);//备注
                cell_remarks.setCellValue(exportWMSData.getRemarks());

                HSSFCell cell_consignNum = sheet.getRow(5).getCell(5);//发货单号
                cell_consignNum.setCellValue(exportWMSData.getConsignNum());

                List<ExportWMSDetail> details = exportWMSData.getDetails();
                int size = details.size();

                //从第12行开始插入(startRow=11)， size 添加插入的行数
                sheet.shiftRows( 11, sheet.getLastRowNum(), size, true, false);

                for(int n=0; n<size; n++){
                    //复制行 n=0,从第11行开始，到第11行结束，定位到第12行(pPostition=11)
                    ExcelUtil.copyRows(sheet, 11, 11, 11+n);
                }

                for(int j = 0; j < details.size(); j++){
                    ExportWMSDetail exportWMSDetail = details.get(j);

                    try{
                        //合同号	品名	型号及规格	送货数量	单位	箱数	箱号
                        HSSFRow row = sheet.getRow(10 + j) == null ? sheet.createRow(10 + j) : sheet.getRow(10 + j);

                        HSSFCell cell_contractNumber = row.getCell(0) == null ? row.createCell(0) : row.getCell(0);//合同号
                        cell_contractNumber.setCellValue(exportWMSDetail.getContractNumber());
                        HSSFCell cell_tradeName = row.getCell(1) == null ? row.createCell(1) : row.getCell(1);//品名
                        cell_tradeName.setCellValue(exportWMSDetail.getTradeName());
                        HSSFCell cell_specification = row.getCell(2) == null ? row.createCell(2) : row.getCell(2);//型号及规格
                        cell_specification.setCellValue(exportWMSDetail.getSpecification());
                        HSSFCell cell_deliveryNumber = row.getCell(3) == null ? row.createCell(3) : row.getCell(3);//送货数量
                        cell_deliveryNumber.setCellValue(exportWMSDetail.getDeliveryNumber());
                        HSSFCell cell_units = row.getCell(4) == null ? row.createCell(4) : row.getCell(4);//单位
                        cell_units.setCellValue(exportWMSDetail.getUnits());
                        HSSFCell cell_boxNumber = row.getCell(5) == null ? row.createCell(5) : row.getCell(5);//箱数
                        cell_boxNumber.setCellValue(exportWMSDetail.getBoxNumber());
                        HSSFCell cell_caseNumber = row.getCell(6) == null ? row.createCell(6) : row.getCell(6);//箱号
                        cell_caseNumber.setCellValue(exportWMSDetail.getCaseNumber());
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("j = "+j);
                    }
                }

                //合计
                HSSFCell cell_totalDeliveryQuantity = sheet.getRow(12+size).getCell(3);//合计(送货数量)
                cell_totalDeliveryQuantity.setCellValue(exportWMSData.getTotalDeliveryQuantity());

                HSSFCell cell_totalBoxes = sheet.getRow(12+size).getCell(5);//合计(箱数)
                cell_totalBoxes.setCellValue(exportWMSData.getTotalBoxes());

                // 输出Excel文件
                OutputStream output = response.getOutputStream();
                String name = StringUtils.toUtf8String("输出装箱清单");
                response.setHeader("Content-Disposition","attachment;filename="+name+".xls");
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
                wb.write(output);
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @ApiOperation(value = "WMS，easyexcel导出，excel")
    @GetMapping("/exportExcel2")
    @ApiOperationSupport(order = 3)
    public void exportExcel2(@RequestParam(value = "userId",required=false) Long userId, HttpServletRequest request, HttpServletResponse response) {
        try {
            userId = (userId != null) ? userId : 1;//userId,不存在，设置为1
            //导出的数据
            String json = redisUtils.get(EXPORTWMSDATALIST+"_"+userId);

            JSONArray jsonArray = JSONArray.parseArray(json);

            //String templateFileName = URLDecoder.decode(this.getClass().getClassLoader().getResource("template_data/model2.xlsx").getPath(), "utf-8");
            //XSSFWorkbook templateWorkbook = new XSSFWorkbook(new FileInputStream(templateFileName));

            ClassPathResource classPathResource = new ClassPathResource("template_data/model2.xlsx");
            InputStream inputStream = classPathResource.getInputStream();
            XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream);

            copyFirstSheet(templateWorkbook, jsonArray.size()-1);

            String sheetNamePrefix = "";
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                String sheetName = sheetNamePrefix + (i + 1);
                templateWorkbook.setSheetName(i, sheetName);
            }

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = "输出装箱清单";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                excelWriter.fill(jsonObject.getJSONArray("details"), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void copyFirstSheet(XSSFWorkbook workbook, int times) {
        if (times <= 0) throw new IllegalArgumentException("times error");
        for (int i = 0; i < times; i++) {
            workbook.cloneSheet(0);
        }
    }


    /**
     * 准备导出的数据
     * @param userId
     * @param file
     */
    private void prepareData(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException("文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        excelList.clear();

        //判断excel是03版本还是07版本
        InputStream is = FileMagic.prepareToCheckMagic(inputStream);
        try {
            FileMagic fm = FileMagic.valueOf(is);
            switch(fm) {
                case OLE2:
                    //excel是03版本
                    Excel03SaxReader reader03 = new Excel03SaxReader(createRowHandler());
                    reader03.read(is, 0);
                    break;
                case OOXML:
                    //excel是07版本
                    Excel07SaxReader reader07 = new Excel07SaxReader(createRowHandler());
                    reader07.read(is, 0);
                    break;
                default:
                    throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //当前登录用户Id
        userId = (userId != null) ? userId : 1;
        //导入前先删除数据,根据登录用户的用户id做删除

        //redisUtils.set(EXCELSPLIT+"_"+userId, JSONObject.toJSONString(excelList), 60*60*2);//保存两小时
        redisUtils.set(EXCELSPLIT+"_"+userId, JSONObject.toJSONString(excelList), -1);


        String s1 = redisUtils.get(EXCELSPLIT+"_"+userId);
        List<List<Object>> list = JSON.parseObject(s1, new TypeReference<List<List<Object>>>() {});


        List<ExportWMSData> exportWMSDataList = new ArrayList<>();

        //以备注作为`拆分明细`计算条件，统计备注
        String countRemarks = null;
        List<ExportWMSDetail> exportWMSDetailList = new ArrayList<>();
        ExportWMSData exportWMSData = new ExportWMSData();

        //int i=1,跳过第一条，表头列
        for(int i=1; i<list.size(); i++){
            List<Object> objects = list.get(i);

            String contractNumber = objects.get(0) != null ? objects.get(0).toString().replaceAll("\\s", "") : null;//合同号
            String caseNumber = objects.get(1) != null ? objects.get(1).toString() : null;//箱号
            String tradeName = objects.get(2) != null ? objects.get(2).toString() : null;//品名
            String specification = objects.get(3) != null ? objects.get(3).toString() : null;//型号及规格
            String units = objects.get(4) != null ? objects.get(4).toString() : null;//单位
            String deliveryNumber = objects.get(5) != null ? objects.get(5).toString() : null;//送货数量
            String boxNumber = objects.get(6) != null ? objects.get(6).toString() : null;//箱数
            String remarks = objects.get(7) != null ? objects.get(7).toString() : null;//备注

            //以备注作为拆分计算条件,备注不为null，并且不是第一个数据
            if(remarks != null){
                if(i != 1){
                    //导出WMS数据
                    exportWMSData.setRemarks(countRemarks);
                    List<ExportWMSDetail> newExportWMSDetailList = new ArrayList<>();
                    newExportWMSDetailList.addAll(exportWMSDetailList);
                    exportWMSData.setDetails(newExportWMSDetailList);
                    BigDecimal totalDeliveryQuantity = new BigDecimal("0");//合计(送货数量)
                    BigDecimal totalBoxes = new BigDecimal("0");//合计(箱数)
                    for(int j = 0; j<newExportWMSDetailList.size(); j++){
                        ExportWMSDetail exportWMSDetail = newExportWMSDetailList.get(j);
                        String det_deliveryNumber = exportWMSDetail.getDeliveryNumber() != null ? exportWMSDetail.getDeliveryNumber() : "0";//送货数量
                        String det_boxNumber = exportWMSDetail.getBoxNumber() != null ? exportWMSDetail.getBoxNumber() : "0";//箱数

                        try{
                            totalDeliveryQuantity = totalDeliveryQuantity.add(new BigDecimal(det_deliveryNumber));
                            totalBoxes = totalBoxes.add(new BigDecimal(det_boxNumber));
                        }catch(Exception e){
                            e.printStackTrace();
                            log.error("java.lang.NumberFormatException: null, det_deliveryNumber: " + det_deliveryNumber);
                        }

                    }
                    exportWMSData.setTotalDeliveryQuantity(totalDeliveryQuantity.toString());
                    exportWMSData.setTotalBoxes(totalBoxes.toString());
                    exportWMSDataList.add(exportWMSData);//保存上一批的数据

                    //清空
                    exportWMSData = new ExportWMSData();
                    exportWMSDetailList.clear();
                }

                countRemarks = remarks;
                //导出WMS明细
                ExportWMSDetail exportWMSDetail = new ExportWMSDetail();
                exportWMSDetail.setContractNumber(contractNumber);
                exportWMSDetail.setTradeName(tradeName);
                exportWMSDetail.setSpecification(specification);
                exportWMSDetail.setDeliveryNumber(deliveryNumber);
                exportWMSDetail.setUnits(units);
                exportWMSDetail.setBoxNumber(boxNumber);
                exportWMSDetail.setCaseNumber(caseNumber);
                exportWMSDetailList.add(exportWMSDetail);

            }else{
                //导出WMS明细
                ExportWMSDetail exportWMSDetail = new ExportWMSDetail();
                exportWMSDetail.setContractNumber(contractNumber);
                exportWMSDetail.setTradeName(tradeName);
                exportWMSDetail.setSpecification(specification);
                exportWMSDetail.setDeliveryNumber(deliveryNumber);
                exportWMSDetail.setUnits(units);
                exportWMSDetail.setBoxNumber(boxNumber);
                exportWMSDetail.setCaseNumber(caseNumber);
                exportWMSDetailList.add(exportWMSDetail);
            }
            //最后一个
            if(i==list.size()-1){
                //导出WMS数据
                exportWMSData.setRemarks(countRemarks);
                List<ExportWMSDetail> newExportWMSDetailList = new ArrayList<>();
                newExportWMSDetailList.addAll(exportWMSDetailList);
                exportWMSData.setDetails(newExportWMSDetailList);
                BigDecimal totalDeliveryQuantity = new BigDecimal("0");//合计(送货数量)
                BigDecimal totalBoxes = new BigDecimal("0");//合计(箱数)
                for(int j = 0; j<newExportWMSDetailList.size(); j++){
                    ExportWMSDetail exportWMSDetail = newExportWMSDetailList.get(j);
                    String det_deliveryNumber = exportWMSDetail.getDeliveryNumber() != null ? exportWMSDetail.getDeliveryNumber() : "0";//送货数量
                    String det_boxNumber = exportWMSDetail.getBoxNumber() != null ? exportWMSDetail.getBoxNumber() : "0";//箱数
                    totalDeliveryQuantity = totalDeliveryQuantity.add(new BigDecimal(det_deliveryNumber));
                    totalBoxes = totalBoxes.add(new BigDecimal(det_boxNumber));

                }
                exportWMSData.setTotalDeliveryQuantity(totalDeliveryQuantity.toString());
                exportWMSData.setTotalBoxes(totalBoxes.toString());
                exportWMSDataList.add(exportWMSData);//保存上一批的数据

                //清空
                exportWMSData = new ExportWMSData();
                exportWMSDetailList.clear();
            }

        }

        Date now = new Date(); // 创建一个Date对象，获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowformat = sdf.format(now);
        System.out.println(nowformat);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
        String deliveryDate = sdf2.format(now);

        for(int j=0; j<exportWMSDataList.size(); j++){
            ExportWMSData data = exportWMSDataList.get(j);
            String remarks = data.getRemarks();
            data.setConsignNum(nowformat+"-"+String.valueOf(j+1));//发货单号
            data.setDeliveryDate(deliveryDate);//送货日期
            data.setCustomerName(remarks);//客户名称
            data.setCustomerAddress(remarks);//客户地址
            data.setConsigneeAndTelephone(remarks);//收货人及电话
        }
        redisUtils.set(EXPORTWMSDATALIST+"_"+userId, JSON.toJSONString(exportWMSDataList, SerializerFeature.DisableCircularReferenceDetect), -1);
    }
}

/**
 * ExportWMSData,导出WMS数据
 */
@Data
class ExportWMSData {

    //发货单号
    @JSONField(ordinal = 1)
    private String consignNum;

    //客户名称
    @JSONField(ordinal = 2)
    private String customerName;

    //送货日期
    @JSONField(ordinal = 3)
    private String deliveryDate;

    //客户地址
    @JSONField(ordinal = 4)
    private String customerAddress;

    //收货人及电话
    @JSONField(ordinal = 5)
    private String consigneeAndTelephone;

    //备注
    @JSONField(ordinal = 6)
    private String remarks;

    //合计(送货数量)
    @JSONField(ordinal = 7)
    private String totalDeliveryQuantity;

    //合计(箱数)
    @JSONField(ordinal = 8)
    private String totalBoxes;

    //明细
    @JSONField(ordinal = 9)
    List<ExportWMSDetail> details;

}

/**
 * ExportWMSDetail,导出WMS明细
 */
@Data
class ExportWMSDetail {

    //合同号
    @JSONField(ordinal = 1)
    private String contractNumber;

    //品名
    @JSONField(ordinal = 2)
    private String tradeName;

    //型号及规格
    @JSONField(ordinal = 3)
    private String specification;

    //送货数量
    @JSONField(ordinal = 4)
    private String deliveryNumber;

    //单位
    @JSONField(ordinal = 5)
    private String units;

    //箱数
    @JSONField(ordinal = 6)
    private String boxNumber;

    //箱号
    @JSONField(ordinal = 7)
    private String caseNumber;

}
