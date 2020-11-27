package com.jayud.common.utils.excel;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成模板实体类
 */
@Data
public class EasyExcelEntity {

    //主标题(多个就是多行标题)
    private List<String> title;

    //台头(同行根据'-'分左右台头)
    private List<String> stageHead;

    //表头
    private LinkedHashMap<String, String> tableHead;

    //表数据
    private JSONArray tableData;

    //合计字符在表格第几列
    private Integer totalIndex;

    //计费项
    private Integer billingItem;

    //合计数据
    private Map<String, BigDecimal> totalData;

    //总合计
    private String totalSumData;

    //表格底部(同行根据'-'分左右台头)
    private List<String> bottomData;

    //工作表名称
    private String sheetName;
}
