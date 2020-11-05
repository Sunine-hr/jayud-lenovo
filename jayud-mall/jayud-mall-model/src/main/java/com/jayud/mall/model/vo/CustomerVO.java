package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerVO {

    @ApiModelProperty(value = "主键Id", position = 1)
    private Integer id;

    @ApiModelProperty(value = "商户ID，第一期默认为2", position = 2)
    private Integer bsUserId;

    @ApiModelProperty(value = "中文名，联系人", position = 3)
    private String nameCn;

    @ApiModelProperty(value = "手机号", position = 3)
    private String phone;

    @ApiModelProperty(value = "启用状态，默认为1，1是0否", position = 5)
    private Integer status;

    @ApiModelProperty(value = "用户等级，等级设计是数值越大等级越高", position = 6)
    @TableField(value = "`level`")
    private Integer level;

    @ApiModelProperty(value = "用户来源，后期可能会有很多用户来源，默认为1web端", position = 7)
    private String origin;

    @ApiModelProperty(value = "创建时间", position = 8)
    private LocalDateTime createDate;

    @ApiModelProperty(value = "公司名", position = 9)
    private String company;

    @ApiModelProperty(value = "审核状态；1是0否，默认为0", position = 10)
    private Boolean auditStatus;

    @ApiModelProperty(value = "代码", position = 11)
    @TableField(value = "`code`")
    private String code;

    @ApiModelProperty(value = "默认结算币种，生成对账单默认选中的结算币种", position = 12)
    private String currency;

    @ApiModelProperty(value = "业务员Id，system_user.id", position = 13)
    private Integer salesmanId;

    @ApiModelProperty(value = "用户名，即登录名", position = 14)
    private String userName;

    @ApiModelProperty(value = "联系地址", position = 15)
    private String address;

}
