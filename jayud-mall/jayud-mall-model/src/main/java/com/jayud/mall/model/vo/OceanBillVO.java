package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "提单信息")
public class OceanBillVO {


    @ApiModelProperty(value = "自增加id")
    private Long id;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "供应商代码(supplier_info supplier_code)")
    private String supplierCode;

    @ApiModelProperty(value = "提单号(供应商提供)")
    private String orderId;

    @ApiModelProperty(value = "起运港口(harbour_info idcode)")
    private String startCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)")
    private String endCode;

    @ApiModelProperty(value = "开船日期")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程")
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)")
    private Integer unit;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务分组id(提单任务task_group id)")
    private Integer taskId;

    /*提单关联柜号信息*/
    @ApiModelProperty(value = "提单关联柜号list")
    private List<OceanCounterVO> oceanCounterVOList;


}
