package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerVO {

    @ApiModelProperty(value = "主键id，自增")
    private Integer id;

    @ApiModelProperty(value = "客户(公司)代码")
    @TableField(value = "`code`")
    private String code;

    @ApiModelProperty(value = "公司名")
    private String company;

    @ApiModelProperty(value = "中文名，联系人")
    private String nameCn;

    @ApiModelProperty(value = "英文名")
    private String nameEn;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户名，即登录名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String passwd;

    @ApiModelProperty(value = "审核状态(0待审核1审核通过2审核不通过，默认为0）")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核人")
    private Integer auditUserId;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "启用状态，默认为1，1是0否")
    @TableField(value = "`status`")
    private Integer status;

    @ApiModelProperty(value = "业务员Id，system_user.id")
    private Integer salesmanId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)")
    private Long operationTeamId;

    @ApiModelProperty(value = "默认结算币种，生成对账单默认选中的结算币种")
    private String currency;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "用户等级，等级设计是数值越大等级越高")
    @TableField(value = "`level`")
    private Integer level;

    @ApiModelProperty(value = "用户来源，后期可能会有很多用户来源，默认为1web端")
    private String origin;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "支付类型")
    private Integer payType;

    @ApiModelProperty(value = "支付密码")
    private String payPasswd;

    @ApiModelProperty(value = "结算方式(1票结 2月结)")
    private Integer clearingWay;

    @ApiModelProperty(value = "结算日期")
    private String clearingDate;

    @ApiModelProperty(value = "锁单日期")
    private String lockDate;

    @ApiModelProperty(value = "实际锁单日期")
    private String realDate;

    @ApiModelProperty(value = "营业执照url")
    private String businessLicenseUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    /****/

}
