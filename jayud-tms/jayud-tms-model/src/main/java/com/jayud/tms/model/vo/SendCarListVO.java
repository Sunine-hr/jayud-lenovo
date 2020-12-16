package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 派车单
 */
@Data
public class SendCarListVO {

    @ApiModelProperty(value = "日期")
    private String createTimeStr;

    @ApiModelProperty(value = "中港车司机")
    private String name;

    @ApiModelProperty(value = "送货时间")
    private String takeTimeStr;

    @ApiModelProperty(value = "派送地址")
    private String address;

    @ApiModelProperty(value = "件数")
    private String pieceAmount;

    @ApiModelProperty(value = "KGS")
    private String weight;

    @ApiModelProperty(value = "备注明细")
    private String remarks;

}
