package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询客户Form")
public class QueryCustomerForm extends BasePageForm{

    @ApiModelProperty(value = "公司名称，客户(公司)名称", position = 1)
    @JSONField(ordinal = 1)
    private String company;

    @ApiModelProperty(value = "用户名，即登录名", position = 2)
    @JSONField(ordinal = 2)
    private String userName;

    @ApiModelProperty(value = "手机号", position = 3)
    @JSONField(ordinal = 3)
    private String phone;

    @ApiModelProperty(value = "关键字(中文名，联系人 or 公司名)", position = 4)
    @JSONField(ordinal = 4)
    private String keyword;

    @ApiModelProperty(value = "审核状态(0待审核1审核通过2审核不通过，默认为0）")
    private Integer auditStatus;

    @ApiModelProperty(value = "用户成员（当前登录人员）")
    private Long memberUserId;


}
