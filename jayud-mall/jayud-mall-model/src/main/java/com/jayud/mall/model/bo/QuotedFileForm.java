package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "报价对应的文件表Form")
public class QuotedFileForm {

    @ApiModelProperty(value = "自增加id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "类型(1报关服务 2清关服务)")
    private String types;

    @ApiModelProperty(value = "文件分组代码(1买单 2独立)", position = 2)
    @JSONField(ordinal = 2)
    private String groupCode;

    @ApiModelProperty(value = "文件分组名称(1买单 2独立)", position = 3)
    @JSONField(ordinal = 3)
    private String groupName;

    @ApiModelProperty(value = "文件代码", position = 4)
    @JSONField(ordinal = 4)
    private String idCode;

    @ApiModelProperty(value = "文件名称", position = 5)
    @JSONField(ordinal = 5)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)", position = 6)
    @JSONField(ordinal = 6)
    @TableField(value = "`options`")
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)", position = 7)
    @JSONField(ordinal = 7)
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址", position = 8)
    @JSONField(ordinal = 8)
    private String templateUrl;

    @ApiModelProperty(value = "说明", position = 9)
    @TableField(value = "`describe`")
    @JSONField(ordinal = 9)
    private String describe;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 10)
    @TableField(value = "`status`")
    @JSONField(ordinal = 10)
    private String status;

    @ApiModelProperty(value = "创建时间", position = 11)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 11, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
