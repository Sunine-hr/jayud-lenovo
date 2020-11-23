package com.jayud.common.utils.excel;

import com.alibaba.excel.util.WorkBookUtil;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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

        setColumnWidth(entity, sheet);

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
        for (int i = 0; i < tableHeads.size(); i++) {
            sheet.setColumnWidth(i, 5000);
        }


    }


    private static Integer setHead(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

        List<String> titles = entity.getTitle();
        //标题
        if (CollectionUtils.isNotEmpty(titles)) {
            for (int i = 0; i < titles.size(); i++) {
                Row row1 = sheet.createRow(i);
                row1.setHeight((short) 800);
                Cell cell0 = row1.createCell(0);
                cell0.setCellValue(titles.get(i));
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle.setAlignment(CENTER);
                if (i != 2) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    font.setFontHeight((short) 400);
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
                    row.createCell(entity.getTableHead().size() - 1).setCellValue(tmps[1]);
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
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(CENTER);
        cellStyle.setBorderBottom(THIN);
        cellStyle.setBorderTop(THIN);
        cellStyle.setBorderRight(THIN);
        cellStyle.setBorderLeft(THIN);


        Row row = sheet.createRow(rowNum);
        //表头
        LinkedHashMap<String, String> tableHead = entity.getTableHead();

        AtomicInteger i = new AtomicInteger();
        tableHead.forEach((k, v) -> {
            row.setHeight((short) 1200);
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

        if (entity.getTableDada() == null) {
            return ++rowNum;
        }

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(CENTER);
        cellStyle.setBorderBottom(THIN);
        cellStyle.setBorderTop(THIN);
        cellStyle.setBorderRight(THIN);
        cellStyle.setBorderLeft(THIN);

        JSONArray tableDadas = entity.getTableDada();

        AtomicInteger j = new AtomicInteger();
        for (int i = 0; i < tableDadas.size(); i++) {
            JSONObject datas = tableDadas.getJSONObject(i);
            Row row = sheet.createRow(rowNum);
            row.setHeight((short) 800);

            entity.getTableHead().forEach((k, v) -> {
                Cell cell = row.createCell(j.get());
                cell.setCellStyle(cellStyle);
                cell.setCellValue(datas.getString(k));
                j.incrementAndGet();
            });
            ++rowNum;
        }
        return rowNum;
    }

    private static Integer tableTotal(EasyExcelEntity entity, Sheet sheet, Workbook workbook, Integer rowNum) {

        if (CollectionUtils.isEmpty(entity.getTotalData())) {
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

        cell0.setCellStyle(cellStyle);
        sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum, rowNum, 0, entity.getTotalIndex()));

        List<String> totalDatas = entity.getTotalData();
        int index = entity.getTotalIndex() + 1;
        for (int i = 0; i < totalDatas.size(); i++) {
            Cell cell = row.createCell(index);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(totalDatas.get(i));
            ++index;
        }


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
                row.setHeight((short) 500);
                row.createCell(0).setCellValue(tmps[0]);
                if (tmps.length > 1) {
                    row.createCell(entity.getTableHead().size() - 1).setCellValue(tmps[1]);
                }
                ++rowNum;
            }
        }

        return rowNum;
    }

    public static void main(String[] args) throws IOException {
        EasyExcelUtils demo = new EasyExcelUtils();

        EasyExcelEntity easyExcelEntity = new EasyExcelEntity();
        easyExcelEntity.setTitle(Arrays.asList("深圳市佳裕达国际货运代理有限公司",
                "客户应收款对帐单", "对帐日期：2020年05月01日到2020年05月31日"));
        easyExcelEntity.setStageHead(Arrays.asList("TO:深圳市伟维运通国际货运代理有限公司",
                "FR:深圳市佳裕达国际货运代理有限公司-账单编号: JYD803_SZ_2020060028"));
//        easyExcelEntity.setTableHead(Arrays.asList("序号", "建单日期", "订单编号", "客户"
//                , "启运地", "目的地", "车牌号", "车型", "件数", "毛重(KGS)", "报关单号"));
        demo.autoGeneration("d://Demo1.xlsx", easyExcelEntity);
    }
}
