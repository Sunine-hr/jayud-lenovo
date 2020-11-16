package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderCustomsFileVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单号(order_info id)")
    private Integer orderId;

    @ApiModelProperty(value = "文件代码")
    private String idCode;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)")
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)")
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    private String describe;

    @ApiModelProperty(value = "审核状态(0审核不通过  1审核通过)")
    private Integer auditStatus;

}
