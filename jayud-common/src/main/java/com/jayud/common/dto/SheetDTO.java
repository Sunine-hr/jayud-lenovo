package com.jayud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * excelUtils包输出多页时使用的封装实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetDTO<T> {
    /**
     * excel sheet 名称
     */
    private String sheetName;
    /**
     * 本页数据实体
     */
    private Class<T> clz;
    /**
     * 数据
     */
    private List<T> data;
    /**
     * 指定要排除的列名
     */
    private List<String> excludeCols;
}
