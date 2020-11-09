package com.jayud.mall.model.po;

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

    @ApiModelProperty(value = "主键id，自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商户ID，第一期默认为2")
    private Integer bsUserId;

    @ApiModelProperty(value = "中文名，联系人")
    private String nameCn;

    @ApiModelProperty(value = "密码")
    private String passwd;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "启用状态，默认为1，1是0否")
    @TableField(value = "`status`")
    private Integer status;

    @ApiModelProperty(value = "用户等级，等级设计是数值越大等级越高")
    @TableField(value = "`level`")
    private Integer level;

    @ApiModelProperty(value = "用户来源，后期可能会有很多用户来源，默认为1web端")
    private String origin;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "公司名")
    private String company;

    @ApiModelProperty(value = "审核状态；1是0否，默认为0")
    private Boolean auditStatus;

    @TableField(value = "`code`")
    private String code;

    @ApiModelProperty(value = "英文名")
    private String nameEn;

    @ApiModelProperty(value = "支付密码")
    private String payPasswd;

    @ApiModelProperty(value = "支付类型")
    private Integer payType;

    @ApiModelProperty(value = "默认结算币种，生成对账单默认选中的结算币种")
    private String currency;

    @ApiModelProperty(value = "业务员Id，system_user.id")
    private Integer salesmanId;

    @ApiModelProperty(value = "用户名，即登录名")
    private String userName;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "结算方式")
    private Integer clearingWay;

    @ApiModelProperty(value = "结算日期")
    private LocalDateTime clearingDate;

    @ApiModelProperty(value = "锁单日期")
    private LocalDateTime lockDate;

    @ApiModelProperty(value = "实际锁单日期")
    private  LocalDateTime realDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
