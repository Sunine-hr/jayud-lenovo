package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * CrmCustomerAddress 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基本档案_客户_地址对象", description = "基本档案_客户_地址")
public class CrmCustomerAddressForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @NotBlank(message = "地址类型不能为空")
    @ApiModelProperty(value = "地址类型")
    private String addressType;

    @ApiModelProperty(value = "国家")
    private String country;

    @NotBlank(message = "省不能为空")
    @ApiModelProperty(value = "省")
    private String province;

    @NotBlank(message = "市不能为空")
    @ApiModelProperty(value = "市")
    private String city;

    @NotBlank(message = "区不能为空")
    @ApiModelProperty(value = "区")
    private String county;

    @NotBlank(message = "详细地址不能为空")
    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "区域(市区，关外等)")
    private String region;

    @NotBlank(message = "联系人不能为空")
    @ApiModelProperty(value = "联系人")
    private String cName;

    @NotBlank(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话")
    private String tel;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "收货公司")
    private String receName;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


}
