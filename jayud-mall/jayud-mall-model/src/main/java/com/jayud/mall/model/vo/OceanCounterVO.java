package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "提单的柜号信息")
public class OceanCounterVO {
    /*柜号信息*/
    @ApiModelProperty(value = "自增加ID", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "柜号", position = 2)
    @JSONField(ordinal = 2)
    private String cntrNo;

    @ApiModelProperty(value = "柜型(cabinet_type id_code)", position = 3)
    @JSONField(ordinal = 3)
    private String cabinetCode;

    @ApiModelProperty(value = "总体积", position = 4)
    @JSONField(ordinal = 4)
    private Double volume;

    @ApiModelProperty(value = "费用", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal cost;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 7)
    @TableField(value = "`status`")
    @JSONField(ordinal = 7)
    private String status;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 8)
    @JSONField(ordinal = 8)
    private Long obId;

    @ApiModelProperty(value = "创建时间", position = 9)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 9, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*柜型基本信息cabinet_type*/
    @ApiModelProperty(value = "柜型基本信息-名称", position = 10)
    @JSONField(ordinal = 10)
    private String cabinetName;

    /*币种currency_info*/
    @ApiModelProperty(value = "币种-币种代码", position = 11)
    @JSONField(ordinal = 11)
    private String ccode;

    @ApiModelProperty(value = "币种-币种名称", position = 12)
    @JSONField(ordinal = 12)
    private String cname;

    /*提单信息*/
    @ApiModelProperty(value = "提单信息-提单号(供应商提供)", position = 13)
    @JSONField(ordinal = 13)
    private String orderId;

    @ApiModelProperty(value = "提单信息-供应商代码(supplier_info supplier_code)", position = 14)
    @JSONField(ordinal = 14)
    private String supplierCode;

    @ApiModelProperty(value = "提单信息-开船日期", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    /*供应商信息*/
    @ApiModelProperty(value = "供应商信息-供应商名称(中)", position = 16)
    @JSONField(ordinal = 16)
    private String supplierChName;

    @ApiModelProperty(value = "费用信息")
    private List<FeeCopeWithVO> feeCopeWithList;

}