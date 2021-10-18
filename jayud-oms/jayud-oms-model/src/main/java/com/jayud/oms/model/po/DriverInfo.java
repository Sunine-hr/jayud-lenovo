package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 供应商对应司机信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DriverInfo对象", description = "供应商对应司机信息")
public class DriverInfo extends Model<DriverInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "司机姓名")
    private String name;

//    @ApiModelProperty(value = "是否主司机(0否 1是)")
//    private String isMain;

    @ApiModelProperty(value = "香港电话")
    private String hkPhone;

    @ApiModelProperty(value = "大陆电话")
    private String phone;

//    @ApiModelProperty(value = "车牌号(vehicle_info id)")
//    private Long vehicleId;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "驾驶证")
    private String drivingNo;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "密码（微信登录使用）")
    private String password;

    @ApiModelProperty(value = "小程序id")
    private String appletId;

    @ApiModelProperty(value = "账户名")
    private String accountName;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "开户行")
    private String accountBank;

    @ApiModelProperty(value = "结算周期(1:当月,2:次月)")
    private Integer billingCycle;

    @ApiModelProperty(value = "账户状态(0:禁用,1:启用)")
    private Integer accountStatus;

    @ApiModelProperty(value = "驾驶证号")
    private String driverLicenseNum;

    @ApiModelProperty(value = "驾龄")
    private Integer drivingAge;

    @ApiModelProperty(value = "初次领证日期")
    private LocalDateTime dateCollection;

    @ApiModelProperty(value = "有效期")
    private LocalDateTime validPeriod;

    @ApiModelProperty(value = "驾驶证档案号")
    private String driverLicenseFileNum;

    @ApiModelProperty(value = "发证机关")
    private String issuingAuthority;

    @ApiModelProperty(value = "驾驶证图片路径")
    private String driverLicenseImg;

    @ApiModelProperty(value = "驾驶证图片名称")
    private String driverLicenseImgName;

    @ApiModelProperty(value = "身份证图片路径")
    private String idCardImg;

    @ApiModelProperty(value = "身份证图片名称")
    private String idCardImgName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
