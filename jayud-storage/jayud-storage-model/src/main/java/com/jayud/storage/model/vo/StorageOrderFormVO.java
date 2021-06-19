package com.jayud.storage.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
@Data
public class StorageOrderFormVO {

    @ApiModelProperty(value = "存仓订单号")
    @ExcelProperty("存仓订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    @ExcelProperty("客户名称")
    private String customerName;

    @ApiModelProperty(value = "出库订单号")
    @ExcelProperty("出库订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "入库批次号")
    @ExcelProperty("入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "sku")
    @ExcelProperty("sku")
    private String sku;

    @ApiModelProperty(value = "存仓时间")
    @ExcelProperty("存仓时间")
    private String startTime;

    @ApiModelProperty(value = "存仓截止时间")
    @ExcelProperty("存仓截止时间")
    private String endTime;

    @ApiModelProperty(value = "所属月份")
    @ExcelProperty("所属月份")
    private String month;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty("创建时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    private String remarks;


}
