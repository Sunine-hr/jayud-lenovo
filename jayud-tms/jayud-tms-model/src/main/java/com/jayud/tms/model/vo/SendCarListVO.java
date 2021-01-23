package com.jayud.tms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 派车单
 */
@Data
public class SendCarListVO {

    @ApiModelProperty(value = "日期")
    private String createTimeStr;

    @ApiModelProperty(value = "中港车司机")
    private String name;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "大陆电话")
    private String phone;

    @ApiModelProperty(value = "入仓号")
    private String enterWarehouseNo;

    @ApiModelProperty(value = "送货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime takeTimeStr;

    @ApiModelProperty(value = "派送地址")
    private String address;

    @ApiModelProperty(value = "件数")
    private String pieceAmount;

    @ApiModelProperty(value = "KGS")
    private String weight;

    @ApiModelProperty(value = "备注明细")
    private String remarks;

}
