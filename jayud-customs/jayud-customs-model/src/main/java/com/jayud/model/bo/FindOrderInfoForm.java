package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 委托单查询form
 *
 * @author william
 * @description
 * @Date: 2020-09-08 11:01
 */
@Data
public class FindOrderInfoForm  {
    @ApiModelProperty(value = "报关行")
    private String bg_org_short;
    @ApiModelProperty(value = "SO号")
    private String so_no;
    @ApiModelProperty(value = "货名")
    private String goods_name;
    @ApiModelProperty(value = "柜号")
    private String container_no1;
    @ApiModelProperty(value = "委托单号")
    private String trust_no;
    @ApiModelProperty(value = "开始委托日期")
    private String created_dt_from;
    @ApiModelProperty(value = "结束委托日期")
    private String created_dt_to;
    @ApiModelProperty(value = "是否发送")
    //（1已发送、0为发送、-1全部）
    private Integer sendYn;
}
