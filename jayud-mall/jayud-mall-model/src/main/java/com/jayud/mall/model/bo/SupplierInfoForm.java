package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplierInfoForm {

    @ApiModelProperty(value = "自增ID", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "供应商代码", position = 2)
    @JSONField(ordinal = 2)
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称(中)", position = 3)
    @JSONField(ordinal = 3)
    private String supplierChName;

    @ApiModelProperty(value = "供应商名称(英)", position = 4)
    @JSONField(ordinal = 4)
    private String supplierEnName;

    @ApiModelProperty(value = "公司名称", position = 5)
    @JSONField(ordinal = 5)
    private String companyName;

    @ApiModelProperty(value = "国家代码(country code)", position = 6)
    @JSONField(ordinal = 6)
    private String stateCode;

    @ApiModelProperty(value = "联系人", position = 7)
    @JSONField(ordinal = 7)
    private String contacts;

    @ApiModelProperty(value = "联系电话", position = 8)
    @JSONField(ordinal = 8)
    private String contactNumber;

    @ApiModelProperty(value = "联系手机", position = 9)
    @JSONField(ordinal = 9)
    private String contactPhone;

    @ApiModelProperty(value = "地址1", position = 10)
    @JSONField(ordinal = 10)
    private String addressFirst;

    @ApiModelProperty(value = "地址2", position = 11)
    @JSONField(ordinal = 11)
    private String addressSecond;

    @ApiModelProperty(value = "地址3", position = 12)
    @JSONField(ordinal = 12)
    private String addressThirdly;

    @ApiModelProperty(value = "省id", position = 13)
    @JSONField(ordinal = 13)
    private Integer pid;

    @ApiModelProperty(value = "省/州名", position = 14)
    @JSONField(ordinal = 14)
    private String pname;

    @ApiModelProperty(value = "城市id", position = 15)
    @JSONField(ordinal = 15)
    private Integer cid;

    @ApiModelProperty(value = "城市名", position = 16)
    @JSONField(ordinal = 16)
    private String cname;

    @ApiModelProperty(value = "邮编", position = 17)
    @JSONField(ordinal = 17)
    private String zipCode;

    @ApiModelProperty(value = "联系地址", position = 18)
    @JSONField(ordinal = 18)
    private String contactAddress;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 19)
    @TableField(value = "`status`")
    @JSONField(ordinal = 19)
    private String status;

    @ApiModelProperty(value = "评级(1-5颗星)", position = 20)
    @JSONField(ordinal = 20)
    private Integer grade;

    @ApiModelProperty(value = "生效日期", position = 21)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 21)
    private LocalDateTime effectiveDate;

    @ApiModelProperty(value = "失效日期", position = 22)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 22)
    private LocalDateTime expiryDate;

    @ApiModelProperty(value = "服务类型List<Long>", position = 23)
    @JSONField(ordinal = 23)
    private List<Long> serviceTypeIds;



}
