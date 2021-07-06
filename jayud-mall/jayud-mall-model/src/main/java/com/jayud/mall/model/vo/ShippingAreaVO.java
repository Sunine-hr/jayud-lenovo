package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShippingAreaVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "仓库代码", position = 2)
    @JSONField(ordinal = 2)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称", position = 3)
    @JSONField(ordinal = 3)
    private String warehouseName;

    @ApiModelProperty(value = "联系人", position = 5)
    @JSONField(ordinal = 5)
    private String contacts;

    @ApiModelProperty(value = "公司名称", position = 6)
    @JSONField(ordinal = 6)
    private String companyName;

    @ApiModelProperty(value = "联系电话", position = 7)
    @JSONField(ordinal = 7)
    private String contactNumber;

    @ApiModelProperty(value = "联系手机", position = 8)
    @JSONField(ordinal = 8)
    private String contactPhone;

    @ApiModelProperty(value = "地址1", position = 9)
    @JSONField(ordinal = 9)
    private String addressFirst;

    @ApiModelProperty(value = "地址2", position = 10)
    @JSONField(ordinal = 10)
    private String addressSecond;

    @ApiModelProperty(value = "地址3", position = 11)
    @JSONField(ordinal = 11)
    private String addressThirdly;


    @ApiModelProperty(value = "国家代码")
    private String countryCode;
    @ApiModelProperty(value = "国家名称")
    private String countryName;
    @ApiModelProperty(value = "省/州代码")
    private String stateCode;
    @ApiModelProperty(value = "省/州名称")
    private String stateName;
    @ApiModelProperty(value = "城市代码")
    private String cityCode;
    @ApiModelProperty(value = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "区县代码")
    private String regionCode;
    @ApiModelProperty(value = "区县名称")
    private String regionName;


    @ApiModelProperty(value = "邮编", position = 16)
    @JSONField(ordinal = 16)
    private String zipCode;

    @ApiModelProperty(value = "进仓单文件模版", position = 17)
    @JSONField(ordinal = 17)
    private String deliveryNote;

    @ApiModelProperty(value = "唛头文件模版URL", position = 18)
    @JSONField(ordinal = 18)
    private String shippingMark;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 19)
    @TableField(value = "`status`")
    @JSONField(ordinal = 19)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 20)
    @JSONField(ordinal = 20)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 21)
    @JSONField(ordinal = 21)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 22)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 22, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
