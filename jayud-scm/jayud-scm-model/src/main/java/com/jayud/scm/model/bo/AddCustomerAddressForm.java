package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户常用地址表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCustomerAddressForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "地址类型(出库，收票等)")
    @JsonProperty(value = "sType")
    private String sType;

    @ApiModelProperty(value = "地址类型(出库，收票等)")
    @JsonProperty(value = "sTypeName")
    private String sTypeName;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "县、区")
    private String county;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "姓名")
    @JsonProperty(value = "cName")
    private String cName;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "手机")
    private String mobileTel;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "QQ")
    private String qq;

    @ApiModelProperty(value = "区域")
    private String region;

    @ApiModelProperty(value = "区域")
    private String regionName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否为默认0否1是")
    private Integer isDefault;

}
