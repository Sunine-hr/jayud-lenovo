package com.jayud.customs.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 委托单-柜号列表
 *
 * @author william
 * @description
 * @Date: 2020-09-07 18:19
 */
@Data
public class CustomsDetail {
    @ApiModelProperty(value = "序号")
    private Integer num_no;
    @ApiModelProperty(value = "柜型")
    //4选1： 20、40、45、53
    private String container_type_no;

    @ApiModelProperty(value = "柜型")
    //2选1：GP、HQ
    private String container_type;

    @ApiModelProperty(value = "柜号")
    private String container_no;

    @ApiModelProperty(value = "是否拼柜")
    //拼柜填写L、整柜填写F
    private String fee_type;
    @ApiModelProperty(value = "封条号")
    private String seal_no;
    @ApiModelProperty(value = "件数")
    private Integer piece;

    @ApiModelProperty(value = "毛重(18,4)")

    private BigDecimal grossweight;

    @ApiModelProperty(value = "品名")
    private String goodsname;

}
