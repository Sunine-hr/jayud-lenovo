package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "编辑客户")
@Data
public class CustomerEditForm {

    @ApiModelProperty(value = "主键id，自增", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "公司名", position = 2)
    @JSONField(ordinal = 2)
    private String company;

    @ApiModelProperty(value = "用户名，即登录名", position = 3)
    @JSONField(ordinal = 3)
    private String userName;

    @ApiModelProperty(value = "手机号", position = 4)
    @JSONField(ordinal = 4)
    private String phone;

    @ApiModelProperty(value = "中文名，联系人", position = 5)
    @JSONField(ordinal = 5)
    private String nameCn;

    @ApiModelProperty(value = "联系电话", position = 6)
    @JSONField(ordinal = 6)
    private String contactNumber;

    @ApiModelProperty(value = "联系地址", position = 7)
    @JSONField(ordinal = 7)
    private String address;

    @ApiModelProperty(value = "营业执照url", position = 8)
    @JSONField(ordinal = 8)
    private String businessLicenseUrl;

}
