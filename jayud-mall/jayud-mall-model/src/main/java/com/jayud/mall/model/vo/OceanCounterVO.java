package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "提单的柜号信息")
public class OceanCounterVO {
    /*柜号信息*/
    @ApiModelProperty(value = "自增加ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜型(cabinet_type idcode)")
    private String cabinetCode;

    @ApiModelProperty(value = "总体积")
    private Double volume;

    @ApiModelProperty(value = "费用")
    private BigDecimal cost;

    @ApiModelProperty(value = "币种(currency_info 表id)")
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Long obId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /*提单信息*/
    @ApiModelProperty(value = "提单信息-提单号(供应商提供)")
    private String orderId;

    @ApiModelProperty(value = "提单信息-供应商代码(supplier_info supplier_code)")
    private String supplierCode;

    @ApiModelProperty(value = "提单信息-开船日期")
    private LocalDateTime sailTime;

    /*供应商信息*/
    @ApiModelProperty(value = "供应商信息-供应商名称(中)")
    private String supplierChName;

    //1个柜子对应N个运单
//    @ApiModelProperty(value = "1个柜子对应N个运单")
//    private List<OceanWaybillVO> oceanWaybillVOList;

}
