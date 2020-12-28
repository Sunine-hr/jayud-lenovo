package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value="SupplierCostVO对象", description="供应商服务费用")
public class SupplierCostVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Long supplierInfoId;

    @ApiModelProperty(value = "服务id(supplier_serve id)", position = 3)
    @JSONField(ordinal = 3)
    private Long serviceId;

    @ApiModelProperty(value = "费用id(cost_item id)", position = 4)
    @JSONField(ordinal = 4)
    private Long costItemId;

    @ApiModelProperty(value = "计算方式(1自动 2手动)", position = 5)
    @JSONField(ordinal = 5)
    private Integer countWay;

    @ApiModelProperty(value = "数量", position = 6)
    @JSONField(ordinal = 6)
    private Integer amount;

    @ApiModelProperty(value = "数量单位(1票 2柜 3方)", position = 7)
    @JSONField(ordinal = 7)
    private Integer amountUnit;

    @ApiModelProperty(value = "数量来源(1固定 2计费重)", position = 8)
    @JSONField(ordinal = 8)
    private Integer amountSource;

    @ApiModelProperty(value = "单价", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer cid;

    @ApiModelProperty(value = "备注", position = 11)
    @JSONField(ordinal = 11)
    private String remark;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 12)
    @JSONField(ordinal = 12)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 14)
    @JSONField(ordinal = 14)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*供应商信息supplier_info*/
    @ApiModelProperty(value = "公司名称", position = 16)
    @JSONField(ordinal = 16)
    private String supplierInfoName;

    /*供应商服务supplier_serve*/
    @ApiModelProperty(value = "服务名称", position = 17)
    @JSONField(ordinal = 17)
    private String serveName;

    /*费用项目cost_item*/
    @ApiModelProperty(value = "费用项目名称", position = 18)
    @JSONField(ordinal = 18)
    private String costItemName;

    /*币种currency_info*/
    @ApiModelProperty(value = "币种代码", position = 19)
    @JSONField(ordinal = 19)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 20)
    @JSONField(ordinal = 20)
    private String currencyName;


}
