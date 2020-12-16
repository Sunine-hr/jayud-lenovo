package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class OrderDataCountVO {

    @ApiModelProperty(value = "暂存数据量")
    private Integer preSubmitCount;

    @ApiModelProperty(value = "全部数据量")
    private Integer allCount;

    @ApiModelProperty(value = "待补全数据量")
    private Integer dataNotAllCount;

    @ApiModelProperty(value = "待处理数据量")
    private Integer pendingCount;

    @ApiModelProperty(value = "待驳回数据量")
    private Integer pendingRejectedCount;

}
