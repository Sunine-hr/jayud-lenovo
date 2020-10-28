package com.jayud.common.utils.excel;

import lombok.Data;

/**
 * excel读取文件页时的配置父类，用于维护一些读取所需的基础信息
 */
@Data
public class ExcelPageBase<T> {
    private Integer startRow = 0;
}
