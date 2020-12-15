package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticsDataNumberVO {

    @ApiModelProperty(value = "id主键")
    private Long deliveryId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

}
