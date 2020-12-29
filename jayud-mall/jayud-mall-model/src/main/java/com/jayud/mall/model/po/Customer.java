package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Customer对象", description="客户表")
public class Customer extends Model<Customer> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自增", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "客户(公司)代码", position = 2)
    @TableField(value = "`code`")
    @JSONField(ordinal = 2)
    private String code;

    @ApiModelProperty(value = "公司名", position = 3)
    @JSONField(ordinal = 3)
    private String company;

    @ApiModelProperty(value = "中文名，联系人", position = 4)
    @JSONField(ordinal = 4)
    private String nameCn;

    @ApiModelProperty(value = "英文名", position = 5)
    @JSONField(ordinal = 5)
    private String nameEn;

    @ApiModelProperty(value = "联系电话", position = 6)
    @JSONField(ordinal = 6)
    private String contactNumber;

    @ApiModelProperty(value = "手机号", position = 7)
    @JSONField(ordinal = 7)
    private String phone;

    @ApiModelProperty(value = "用户名，即登录名", position = 8)
    @JSONField(ordinal = 8)
    private String userName;

    @ApiModelProperty(value = "密码", position = 9)
    @JSONField(ordinal = 9)
    private String passwd;

    @ApiModelProperty(value = "审核状态(0待审核1审核通过2审核不通过，默认为0）", position = 10)
    @JSONField(ordinal = 10)
    private Integer auditStatus;

    @ApiModelProperty(value = "审核人", position = 11)
    @JSONField(ordinal = 11)
    private Integer auditUserId;

    @ApiModelProperty(value = "审核时间", position = 12)
    @JSONField(ordinal = 12)
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "启用状态，默认为1，1是0否", position = 13)
    @TableField(value = "`status`")
    @JSONField(ordinal = 13)
    private Integer status;

    @ApiModelProperty(value = "业务员Id，system_user.id", position = 14)
    @JSONField(ordinal = 14)
    private Integer salesmanId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 15)
    @JSONField(ordinal = 15)
    private Long operationTeamId;

    @ApiModelProperty(value = "默认结算币种，生成对账单默认选中的结算币种(currency_info currency_code)", position = 16)
    @JSONField(ordinal = 16)
    private String currency;

    @ApiModelProperty(value = "联系地址", position = 17)
    @JSONField(ordinal = 17)
    private String address;

    @ApiModelProperty(value = "用户等级，等级设计是数值越大等级越高", position = 18)
    @TableField(value = "`level`")
    @JSONField(ordinal = 18)
    private Integer level;

    @ApiModelProperty(value = "用户来源，后期可能会有很多用户来源，默认为1web端", position = 19)
    @JSONField(ordinal = 19)
    private String origin;

    @ApiModelProperty(value = "创建日期", position = 20)
    @JSONField(ordinal = 20)
    private LocalDateTime createDate;

    @ApiModelProperty(value = "支付类型", position = 21)
    @JSONField(ordinal = 21)
    private Integer payType;

    @ApiModelProperty(value = "支付密码", position = 22)
    @JSONField(ordinal = 22)
    private String payPasswd;

    @ApiModelProperty(value = "结算方式(1票结 2月结)", position = 23)
    @JSONField(ordinal = 23)
    private Integer clearingWay;

    @ApiModelProperty(value = "结算日期", position = 24)
    @JSONField(ordinal = 24)
    private String clearingDate;

    @ApiModelProperty(value = "锁单日期", position = 25)
    @JSONField(ordinal = 25)
    private String lockDate;

    @ApiModelProperty(value = "实际锁单日期", position = 26)
    @JSONField(ordinal = 26)
    private String realDate;

    @ApiModelProperty(value = "营业执照url", position = 27)
    @JSONField(ordinal = 27)
    private String businessLicenseUrl;

    @ApiModelProperty(value = "备注", position = 28)
    @JSONField(ordinal = 28)
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
