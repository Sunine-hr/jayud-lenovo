package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillLogisticsTrackVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "关联提单ID(ocean_bill id)")
    private String billId;

    @ApiModelProperty(value = "状态码")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusName;

    @ApiModelProperty(value = "本次节点的操作描述")
    private String description;

    @ApiModelProperty(value = "当前节点信息发生时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人id(system_user id)")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称(system_user name)")
    private String operatorName;

    @ApiModelProperty(value = "备注")
    private String remark;

}
