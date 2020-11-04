package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerVO {

    @ApiModelProperty(value = "主键Id")
    private Integer id;

    @ApiModelProperty(value = "商户ID，第一期默认为2")
    private Integer bsUserId;

    @ApiModelProperty(value = "中文名，联系人")
    private String nameCn;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "启用状态，默认为1，1是0否")
    private Integer status;

    @ApiModelProperty(value = "用户等级，等级设计是数值越大等级越高")
    @TableField(value = "`level`")
    private Integer level;

    @ApiModelProperty(value = "用户来源，后期可能会有很多用户来源，默认为1web端")
    private String origin;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "公司名")
    private String company;

    @ApiModelProperty(value = "审核状态；1是0否，默认为0")
    private Boolean auditStatus;

    @TableField(value = "`code`")
    private String code;

    @ApiModelProperty(value = "默认结算币种，生成对账单默认选中的结算币种")
    private String currency;

    @ApiModelProperty(value = "业务员Id，system_user.id")
    private Integer salesmanId;

    @ApiModelProperty(value = "用户名，即登录名")
    private String userName;

    @ApiModelProperty(value = "联系地址")
    private String address;

}
