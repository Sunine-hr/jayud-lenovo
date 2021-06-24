package com.jayud.common.utils.excel.style;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

public class History2014ExportCellWriteHandler implements CellWriteHandler {
    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        if (cell.getRowIndex() == 0) {
            font.setFontHeightInPoints((short) 18);
            font.setFontName("宋体");
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置 自动换行
            cellStyle.setWrapText(true);
            Row row = cell.getRow();
            row.setHeightInPoints(27);
        } else if (cell.getRowIndex() == 1) {
            font.setFontHeightInPoints((short) 11);
            font.setFontName("宋体");
            cellStyle.setBorderBottom(BorderStyle.NONE);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            font.setBold(false);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置 自动换行
            cellStyle.setWrapText(true);

            Row row = cell.getRow();
            row.setHeightInPoints(27);
        } else if (cell.getRowIndex() == 2) {
            //设置边框
            cellStyle.setBorderBottom(BorderStyle.NONE);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            //设置字体大小
            font.setFontHeightInPoints((short) 12);
            //设置字体样式
            font.setFontName("宋体");
            //设置加粗
            font.setBold(false);
            //设置字体样式
            cellStyle.setFont(font);
            //设置  文字左右居中  【水平居中需要使用以下两行】
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //设置文字居中  上下居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //设置 自动换行
            cellStyle.setWrapText(true);
            //获取行
            Row row = cell.getRow();
            //设计行高
            row.setHeightInPoints(27);

        } else {
            //设置边框
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            //居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Row row = cell.getRow();
            //设计行高
            row.setHeightInPoints(27);
        }
        cell.setCellStyle(cellStyle);
    }
}
