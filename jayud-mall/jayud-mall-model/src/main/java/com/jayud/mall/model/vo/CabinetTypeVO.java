package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CabinetTypeVO {

    @ApiModelProperty(value = "自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "柜型号名称", position = 2)
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "编码编码", position = 3)
    @JSONField(ordinal = 3)
    private String idCode;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

    @ApiModelProperty(value = "创建时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "简称", position = 6)
    @JSONField(ordinal = 6)
    private String abbreviation;

    @ApiModelProperty(value = "容积，实际可装货的容积，单位CBM", position = 7)
    @JSONField(ordinal = 7)
    private Double cubage;

    @ApiModelProperty(value = "外径，长，单位英尺", position = 8)
    @JSONField(ordinal = 8)
    private Double outerdiameterLong;

    @ApiModelProperty(value = "外径，宽，单位英尺", position = 9)
    @JSONField(ordinal = 9)
    private Double outerdiameterWidth;

    @ApiModelProperty(value = "外径，高，单位英尺", position = 10)
    @JSONField(ordinal = 10)
    private Double outerdiameterHigh;


}
