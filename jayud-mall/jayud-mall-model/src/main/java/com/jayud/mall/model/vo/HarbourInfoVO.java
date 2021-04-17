package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HarbourInfoVO {

    @ApiModelProperty(value = "自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "代码", position = 2)
    @JSONField(ordinal = 2)
    private String idCode;

    @ApiModelProperty(value = "中文名称", position = 3)
    @JSONField(ordinal = 3)
    private String codeName;

    @ApiModelProperty(value = "英文名称", position = 4)
    @JSONField(ordinal = 4)
    private String codeNameEn;

    @ApiModelProperty(value = "国家代码(country code)", position = 5)
    @JSONField(ordinal = 5)
    private String stateCode;

    @ApiModelProperty(value = "地址一", position = 6)
    @JSONField(ordinal = 6)
    private String addressFirst;

    @ApiModelProperty(value = "地址二", position = 7)
    @JSONField(ordinal = 7)
    private String addressSecond;

    @ApiModelProperty(value = "地址三", position = 8)
    @JSONField(ordinal = 8)
    private String addressThirdly;

    @ApiModelProperty(value = "省id", position = 9)
    @JSONField(ordinal = 9)
    private Integer pid;

    @ApiModelProperty(value = "省/州名", position = 10)
    @JSONField(ordinal = 10)
    private String pname;

    @ApiModelProperty(value = "城市id", position = 11)
    @JSONField(ordinal = 11)
    private Integer cid;

    @ApiModelProperty(value = "城市名", position = 12)
    @JSONField(ordinal = 12)
    private String cname;

    @ApiModelProperty(value = "邮编", position = 13)
    @JSONField(ordinal = 13)
    private String zipCode;

    @ApiModelProperty(value = "类型(1机场 2港口)", position = 14)
    @JSONField(ordinal = 14)
    private Integer genre;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 15)
    @TableField(value = "`status`")
    @JSONField(ordinal = 15)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 16)
    @JSONField(ordinal = 16)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 17)
    @JSONField(ordinal = 17)
    private Integer userName;

    @ApiModelProperty(value = "创建时间", position = 18)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 18, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
