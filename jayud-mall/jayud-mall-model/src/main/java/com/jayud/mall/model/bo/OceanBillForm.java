package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "提单")
public class OceanBillForm {

    @ApiModelProperty(value = "自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer tid;

    @ApiModelProperty(value = "供应商代码(supplier_info supplier_code)", position = 3)
    @JSONField(ordinal = 3)
    private String supplierCode;

    @ApiModelProperty(value = "提单号(供应商提供)", position = 4)
    @JSONField(ordinal = 4)
    private String orderId;

    @ApiModelProperty(value = "起运港口(harbour_info idcode)", position = 5)
    @JSONField(ordinal = 5)
    private String startCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)", position = 6)
    @JSONField(ordinal = 6)
    private String endCode;

    @ApiModelProperty(value = "开船日期", position = 7)
    @JSONField(ordinal = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程", position = 8)
    @JSONField(ordinal = 8)
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)", position = 9)
    @JSONField(ordinal = 9)
    private Integer unit;

    @ApiModelProperty(value = "创建时间", position = 10)
    @JSONField(ordinal = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务分组id(提单任务task_group id)", position = 11)
    @JSONField(ordinal = 11)
    private Integer taskId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 12)
    @JSONField(ordinal = 12)
    private Integer operationTeamId;

    //1提单对应1货柜，(PS:之前是1提单对应N货柜，现在还是用list，不改了，限制list的大小为1)
    @ApiModelProperty(value = "提单对应货柜信息list", position = 13)
    @JSONField(ordinal = 12)
    private List<OceanCounterForm> oceanCounterForms;

}
