package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "提单")
public class OceanBillForm {

    @ApiModelProperty(value = "自增加id")
    @JSONField(ordinal = 0)
    private Long id;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    @JSONField(ordinal = 1)
    private Integer tid;

    @ApiModelProperty(value = "供应商代码(supplier_info supplier_code)")
    @JSONField(ordinal = 2)
    private String supplierCode;

    @ApiModelProperty(value = "提单号(供应商提供)")
    @JSONField(ordinal = 3)
    private String orderId;

    @ApiModelProperty(value = "起运港口(harbour_info idcode)")
    @JSONField(ordinal = 4)
    private String startCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)")
    @JSONField(ordinal = 5)
    private String endCode;

    @ApiModelProperty(value = "开船日期")
    @JSONField(ordinal = 6)
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程")
    @JSONField(ordinal = 7)
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)")
    @JSONField(ordinal = 8)
    private Integer unit;

    @ApiModelProperty(value = "创建时间")
    @JSONField(ordinal = 9)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务分组id(提单任务task_group id)")
    @JSONField(ordinal = 10)
    private Integer taskId;

    //1提单对应1货柜，(PS:之前是1提单对应N货柜，现在还是用list，不改了，限制list的大小为1)
    @ApiModelProperty(value = "提单对应货柜信息list")
    @JSONField(ordinal = 11)
    private List<OceanCounterForm> oceanCounterForms;

}
