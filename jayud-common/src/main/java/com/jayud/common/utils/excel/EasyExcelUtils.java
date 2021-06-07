package com.jayud.common.utils.excel;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.util.WorkBookUtil;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.jayud.common.entity.InitChangeStatusVO;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.poi.ss.usermodel.BorderStyle.THIN;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;

public class EasyExcelUtils {

    public final static String SPLIT_SYMBOL = "~";

    public static Workbook autoGeneration(String path, EasyExcelEntity entity) throws IOException {
        // 文件输出位置
//        OutputStream out = new FileOutputStream(path);

        WriteWorkbook writeWorkbook = new WriteWorkbook();
        WriteWorkbookHolder writeWorkbookHolder = new WriteWorkbookHolder(writeWorkbook);
        WorkBookUtil.createWorkBook(writeWorkbookHolder);
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.createSheet(entity.getSheetName() == null ? "sheel" : entity.getSheetName());

        //当前行
        Integer rowNum = 0;
        //表格顶部标题
        rowNum = setHead(entity, sheet, workbook, rowNum);
        //表头
        rowNum = tableHead(entity, sheet, workbook, rowNum);
        //表数据
        rowNum = contentData(entity, sheet, workbook, rowNum);
        //合计
        rowNum = tableTotal(entity, sheet, workbook, rowNum);

        //总合计
        rowNum = tableTotalSum(entity, sheet, workbook, rowNum);

        //顶部数据
        rowNum = setTop(entity, sheet, workbook, rowNum);

        setColumnWidth(entity, sheet);

        // 处理中文不能自动调整列宽的问题
//        setSizeColumn(sheet, entity.getTableHead().size());
//        workbook.write(out);
//        workbook.close();

        return workbook;

    }

    //设置列宽度
    private static void setColumnWidth(EasyExcelEntity entity, Sheet sheet) {
        LinkedHashMap<String, String> tableHeads = entity.getTableHead();
        if (tableHeads == null) {
            return;
        }
        SXSSFSheet tmp = (SXSSFSheet) sheet;
        tmp.trackAllColumnsForAutoSizing();
        for (int i = 1; i <= tableHeads.size(); i++) {
//            sheet.autoSizeColumn((short) i); //调整第一列宽度
//            sheet.setColumnWidth((short) i, sheet.getColumnWidth(i) * 17 / 10);

            sheet.setColumnWidth(i, 338 / entity.getTableHead().size() * 100);
        }

        //页边距
        sheet.setMargin(HSSFSheet.TopMargin, 1.0);// 页边距（上）
        sheet.setMargin(HSSFSheet.BottomMargin, 1.0);// 页边距（下）
        sheet.setMargin(HSSFSheet.LeftMargin, 0.5);// 页边距（左）
        sheet.setMargin(HSSFSheet.RightMargin, 0.5);// 页边距（右)
        sheet.setMargin(HSSFSheet.HeaderMargin, 0.76);//页脚
        sheet.setMargin(HSSFSheet.FooterMargin, 0.76);//页脚


    }


    private static Integer setHead(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

        List<String> titles = entity.getTitle();
        //标题
        if (CollectionUtils.isNotEmpty(titles)) {
            for (int i = 0; i < titles.size(); i++) {
                Row row1 = sheet.createRow(i);
                row1.setHeight((short) 500);
                Cell cell0 = row1.createCell(0);
                cell0.setCellValue(titles.get(i));
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle.setAlignment(CENTER);
                if (i != 2) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    font.setFontHeight((short) 300);
                    cellStyle.setFont(font);
                }
                cell0.setCellStyle(cellStyle);
                sheet.addMergedRegionUnsafe(new CellRangeAddress(i, i, 0, entity.getTableHead().size() - 1));
                ++rowNum;
            }
        }
        //抬头
        if (CollectionUtils.isNotEmpty(entity.getStageHead())) {
            List<String> stageHeads = entity.getStageHead();
            for (int i = 0; i < stageHeads.size(); i++) {
                String stageHead = stageHeads.get(i);

                String[] tmps = stageHead.split(SPLIT_SYMBOL);

                Row row = sheet.createRow(rowNum);
                row.setHeight((short) 500);
                row.createCell(0).setCellValue(tmps[0]);
                if (tmps.length > 1) {
                    int index = tmps.length == 3 ? Integer.parseInt(tmps[2]) : 5;
                    row.createCell(entity.getTableHead().size() - index).setCellValue(tmps[1]);
                }
                ++rowNum;
            }

        }

        return rowNum;
    }

    private static Integer tableHead(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {
        if (entity.getTableHead() == null) {
            return rowNum;
        }
        //样式
        CellStyle cellStyle = workbook.createCellStyle();
//        XSSFCellStyle xsStyle = (XSSFCellStyle)cellStyle;
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(CENTER);
        cellStyle.setBorderBottom(THIN);
        cellStyle.setBorderTop(THIN);
        cellStyle.setBorderRight(THIN);
        cellStyle.setBorderLeft(THIN);
        cellStyle.setWrapText(true);


        Row row = sheet.createRow(rowNum);
        //表头
        LinkedHashMap<String, String> tableHead = entity.getTableHead();

        AtomicInteger i = new AtomicInteger();
        tableHead.forEach((k, v) -> {
//            row.setHeight((short) 1200);
            Cell cell = row.createCell(i.get());
            cell.setCellStyle(cellStyle);
            cell.setCellValue(tableHead.get(k));
            i.incrementAndGet();
        });

        return ++rowNum;
    }


    private static Integer contentData(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

//        if (CollectionUtils.isEmpty(entity.getTableDada())) {
//            return ++rowNum;
//        }

        if (entity.getTableData() == null) {
            return ++rowNum;
        }

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(CENTER);
        cellStyle.setBorderBottom(THIN);
        cellStyle.setBorderTop(THIN);
        cellStyle.setBorderRight(THIN);
        cellStyle.setBorderLeft(THIN);
        cellStyle.setWrapText(true);

        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);


        JSONArray tableDatas = entity.getTableData();


        for (int i = 0; i < tableDatas.size(); i++) {
            JSONObject datas = tableDatas.getJSONObject(i);
            Row row = sheet.createRow(rowNum);
            row.setHeight((short) 800);
            AtomicInteger j = new AtomicInteger();
            entity.getTableHead().forEach((k, v) -> {
                int cellNum = j.get();
                Cell cell = row.createCell(cellNum);
                cell.setCellStyle(cellStyle);
                String data = datas.getStr(k);
                if (entity.getTotalIndex() != null) { //费用等与空返回0
                    if (cellNum > entity.getTotalIndex()) {
                        data = data == null ? "0" : data;
                    }
                }
                cell.setCellValue(data);
                j.incrementAndGet();
            });
            ++rowNum;
        }
        return rowNum;
    }

    private static Integer tableTotal(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

        if (entity.getTotalData() == null) {
            return rowNum;
        }

        Row row = sheet.createRow(rowNum);
        row.setHeight((short) 800);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("合计");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setBorderBottom(THIN);
        cellStyle.setBorderTop(THIN);
        cellStyle.setBorderRight(THIN);
        cellStyle.setBorderLeft(THIN);
        cellStyle.setWrapText(true);

        CellRangeAddress cellAddresses = new CellRangeAddress(rowNum, rowNum, 0, entity.getTotalIndex());

        RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellAddresses, sheet);

        sheet.addMergedRegionUnsafe(cellAddresses);

        cell0.setCellStyle(cellStyle);

        Map<String, BigDecimal> totalDatas = entity.getTotalData();
        AtomicInteger index = new AtomicInteger(entity.getTotalIndex() + 1);

        entity.getTableHead().forEach((k, v) -> {
            BigDecimal cost = totalDatas.get(k);
            if (cost != null) {
                Cell cell = row.createCell(index.get());
                cell.setCellStyle(cellStyle);
                cell.setCellValue(cost.toString());
                index.incrementAndGet();
            }
        });

        return ++rowNum;
    }


    private static Integer tableTotalSum(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

        if (StringUtils.isEmpty(entity.getTotalSumData())) {
            return rowNum;
        }

        Row row = sheet.createRow(rowNum);
        row.setHeight((short) 800);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("总合计");


        CellRangeAddress cellAddresses = new CellRangeAddress(rowNum, rowNum,
                0, entity.getTableHead().size() - 3);
        sheet.addMergedRegionUnsafe(cellAddresses);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setWrapText(true);

        cell0.setCellStyle(cellStyle);

        RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellAddresses, sheet);

        Cell cell = row.createCell(24);

        cellAddresses = new CellRangeAddress(rowNum, rowNum,
                entity.getTotalIndex() + entity.getBillingItem() + 1,
                entity.getTableHead().size() - 1);
        sheet.addMergedRegionUnsafe(cellAddresses);

        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setAlignment(CENTER);
        cell.setCellStyle(cellStyle1);

        RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddresses, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellAddresses, sheet);

        cell.setCellValue(entity.getTotalSumData());

        return ++rowNum;
    }

    private static Integer setTop(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

        List<String> bottomDatas = entity.getBottomData();

        if (CollectionUtils.isNotEmpty(bottomDatas)) {

            for (int i = 0; i < bottomDatas.size(); i++) {
                String bottomData = bottomDatas.get(i);
                String[] tmps = bottomData.split(SPLIT_SYMBOL);
                Row row = sheet.createRow(rowNum);
                row.setHeight((short) 400);
                row.createCell(0).setCellValue(tmps[0]);
                if (tmps.length > 1) {
                    row.createCell(entity.getTableHead().size() - 5).setCellValue(tmps[1]);
                }
                ++rowNum;
            }
        }

        return rowNum;
    }


    // 自适应宽度(中文支持)
    public static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 1; columnNum <= size; columnNum++) {
//            int columnWidth = 369 / size;
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                //自动调整宽度
                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }


            sheet.setColumnWidth(columnNum, columnWidth * 100);

        }
    }


    public void complexFill(String json, HttpServletResponse response) throws IOException {

        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // {} 代表普通变量 {.} 代表是list的变量
        String templateFileName = URLDecoder.decode(this.getClass().getClassLoader().getResource("templates/" + "填充模板.xlsx").getPath(), "utf-8");
        // create template work book
        XSSFWorkbook templateWorkbook = new XSSFWorkbook(new FileInputStream(templateFileName));
        // copy sheet
        JSONArray jsonArray = new JSONArray(json);
        copyFirstSheet(templateWorkbook, jsonArray.size() - 1);
        // update sheet name
        String sheetNamePrefix = "Sheet-";
        for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
            String sheetName = sheetNamePrefix + (i + 1);
            templateWorkbook.setSheetName(i, sheetName);
        }

        // outStream to inStream
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        templateWorkbook.write(outStream);
        ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

        String fileName = "单据.xlsx";

        ExcelWriter excelWriter = null;
        if (response != null) { //网络流下载
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
        } else {
            excelWriter = EasyExcel.write(fileName).withTemplate(templateInputStream).build();
        }

        // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
        // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
        // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
        // 如果数据量大 list不是最后一行 参照下一个

        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
            WriteSheet sheet = EasyExcel.writerSheet(i).build();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            excelWriter.fill(jsonObject.getJSONArray("details"), fillConfig, sheet);
            excelWriter.fill(jsonObject, sheet);
        }

        excelWriter.finish();

    }

    @SneakyThrows
    public static <T> void fillTemplate(JSONObject json, Map<String, List<T>> list,
                                        String templateFilePath, String outputPath, String fileName, HttpServletResponse response) {
        String templateFileName = URLDecoder.decode(templateFilePath, "utf-8");

        XSSFWorkbook templateWorkbook = new XSSFWorkbook(new FileInputStream(templateFileName));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        templateWorkbook.write(outStream);
        ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

        ExcelWriter excelWriter = null;
        if (response != null) { //网络流下载
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
        } else {
            excelWriter = EasyExcel.write(outputPath).withTemplate(templateInputStream).build();
        }
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。但是这个就会把所有数据放到内存 会很耗内存
//        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 data1，然后多个list必须用 FillWrapper包裹
        ExcelWriter finalExcelWriter = excelWriter;
        list.forEach((k, v) -> {
            finalExcelWriter.fill(new FillWrapper(k, v), writeSheet);
        });

        Map<String, Object> map = new HashMap<>();
        json.forEach(map::put);
        finalExcelWriter.fill(map, writeSheet);
        // 别忘记关闭流
        finalExcelWriter.finish();
    }


//    public static void main(String[] args) throws IOException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("customer", "深圳客户");
//        jsonObject.put("optUser", "张三");
//        jsonObject.put("mainOrderNo", "JY10002046");
//        jsonObject.put("subOrderNo", "ZG0002154");
//        jsonObject.put("takeTime", "2021/6/2");
//        jsonObject.put("totalNum", "200");
//        jsonObject.put("totalWeight", "5");
//        jsonObject.put("bizType", "跨境运输+内陆");
//        Map<String, List> list = new HashMap<>();
//        fillTemplate(jsonObject, list, "D:\\公司\\模板\\20210601工作表.xlsx");
//    }


    public static void copyFirstSheet(XSSFWorkbook workbook, int times) {
        if (times <= 0) throw new IllegalArgumentException("times error");
        for (int i = 0; i < times; i++) {
            workbook.cloneSheet(0);
        }
    }
}
