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
    private String sheetName;
    private Class<T> clz;
    private List<T> data;
}
