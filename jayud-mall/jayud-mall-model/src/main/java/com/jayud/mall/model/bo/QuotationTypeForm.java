package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuotationTypeForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价类型代码", position = 2)
    @JSONField(ordinal = 2)
    private String code;

    @ApiModelProperty(value = "报价类型名称", position = 2)
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "父级id", position = 3)
    @JSONField(ordinal = 3)
    private Integer fid;

    @ApiModelProperty(value = "排序", position = 4)
    @JSONField(ordinal = 4)
    private Integer sort;

    @ApiModelProperty(value = "创建人(system_user id)", position = 5)
    @JSONField(ordinal = 5)
    private String createBy;

    @ApiModelProperty(value = "创建时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人(system_user id)", position = 7)
    @JSONField(ordinal = 7)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", position = 8)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 8, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用", position = 9)
    @JSONField(ordinal = 9)
    private String status;

    @ApiModelProperty(value = "类型1整柜 2拼箱", position = 10)
    @JSONField(ordinal = 10)
    private String types;

}
