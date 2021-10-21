package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 供应商对应司机信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class AddDriverInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "司机姓名")
    @NotEmpty(message = "请填写司机姓名")
    private String name;

//    @ApiModelProperty(value = "是否主司机(0否 1是)")
//    @NotEmpty(message = "isMain is required")
//    private String isMain;

    @ApiModelProperty(value = "香港电话")
    private String hkPhone;

    @ApiModelProperty(value = "大陆电话")
    @NotEmpty(message = "请填写大陆电话")
    private String phone;

//    @ApiModelProperty(value = "车牌号id")
//    @NotNull(message = "vehicleId is required")
//    private Long vehicleId;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "驾驶证")
    private String drivingNo;


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
    private String drivingAge;

    @ApiModelProperty(value = "初次领证日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCollection;

    @ApiModelProperty(value = "有效期")
    @NotNull(message = "请填写有效期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validPeriod;

    @ApiModelProperty(value = "驾驶证档案号")
    private String driverLicenseFileNum;

    @ApiModelProperty(value = "发证机关")
    private String issuingAuthority;

    @ApiModelProperty(value = "驾驶证图片路径")
    private List<FileView> driverLicenseImgs;

    @ApiModelProperty(value = "身份证图片路径")
    private List<FileView> idCardImgs;


    //    @ApiModelProperty(value = "密码")
//    @NotEmpty(message = "password is required")
//    public String password;

}
