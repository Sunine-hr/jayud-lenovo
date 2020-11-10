package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerForm {

    @ApiModelProperty(value = "主键id，自增")
    private Integer id;

    @ApiModelProperty(value = "公司名,客户名称")
    private String company;

    @ApiModelProperty(value = "用户名，即登录名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "中文名，联系人")
    private String nameCn;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系地址")
    private String address;


}
