package com.jayud.storage.model.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 仓储商品信息表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
// 头背景设置成红色 IndexedColors.RED.getIndex()
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 10)
// 头字体设置成20
@HeadFontStyle(fontHeightInPoints = 16)

@ContentRowHeight(20)
@HeadRowHeight(30)
@ColumnWidth(25)
public class WarehouseGoodsInForm{

    @ApiModelProperty(value = "预计到达时间")
    @ExcelProperty("预计到达时间")
    @DateTimeFormat("yyyy:MM:dd HH:mm:ss")
    private LocalDateTime estimatedArrivalTime;

//    @ApiModelProperty(value = "商品名称")
//    private String name;

    @ApiModelProperty(value = "sku")
    @ExcelProperty("sku")
    private String sku;

//    @ApiModelProperty(value = "规格型号")
//    private String specificationModel;

    @ApiModelProperty(value = "板数")
    @ExcelProperty("板数")
    private Integer boardNumber;

    @ApiModelProperty(value = "件数")
    @ExcelProperty("件数")
    private Integer number;

    @ApiModelProperty(value = "pcs")
    @ExcelProperty("pcs")
    private Integer pcs;

    @ApiModelProperty(value = "重量")
    @ExcelProperty("重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    @ExcelProperty("体积")
    private Double volume;

    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    private String remarks;

}
