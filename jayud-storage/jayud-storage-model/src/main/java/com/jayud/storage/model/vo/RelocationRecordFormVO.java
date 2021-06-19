package com.jayud.storage.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 移库信息表
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RelocationRecordFormVO {


    @ApiModelProperty(value = "移库订单号")
    @ExcelProperty("移库订单号")
    private String orderNo;

    @ApiModelProperty(value = "仓库名称")
    @ExcelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "区域名称")
    @ExcelProperty("区域名称")
    private String areaName;

    @ApiModelProperty(value = "货架名称")
    @ExcelProperty("货架名称")
    private String shelvesName;

    @ApiModelProperty(value = "旧库位")
    @ExcelProperty("旧库位")
    private String oldLocationCode;

    @ApiModelProperty(value = "新库位")
    @ExcelProperty("新库位")
    private String newLocationCode;

    @ApiModelProperty(value = "sku")
    @ExcelProperty("sku")
    private String sku;

    @ApiModelProperty(value = "移库商品件数")
    @ExcelProperty("移库商品件数")
    private Integer number;

    @ApiModelProperty(value = "创建人(登录用户)")
    @ExcelProperty("创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    private String remarks;



}
