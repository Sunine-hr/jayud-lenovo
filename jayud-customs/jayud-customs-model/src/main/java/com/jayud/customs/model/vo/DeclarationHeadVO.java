package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询报关进程VO-head
 *
 * @author william
 * @description
 * @Date: 2020-09-09 17:16
 */
@Data
public class DeclarationHeadVO {
    @ApiModelProperty(value = "进出口岸")
    private String port_no;
    @ApiModelProperty(value = "18位报关单号")
    private String custom_apply_no;
    @ApiModelProperty(value = "提运单号")
    private String cabin_no;
    @ApiModelProperty(value = "境内收发货人名称")
    private String shipper_name;
    @ApiModelProperty(value = "柜号")
    private String container_no1;
    @ApiModelProperty(value = "柜量")
    private String container_count;
    @ApiModelProperty(value = "商品数")
    private String num_goods;
    @ApiModelProperty(value = "品名")
    private String goods_name;
}
