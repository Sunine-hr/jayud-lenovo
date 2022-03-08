package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.JayudBizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

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

    @ApiModelProperty(value = "地址类型")
    private String addressType;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String county;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "区域(市区，关外等)")
    private String region;

    @ApiModelProperty(value = "联系人")
    private String contactName;

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
    private Boolean isDeleted;


    /**
     * 校验参数
     */
    public void checkParam() {
        if (StringUtils.isEmpty(address)){throw new JayudBizException(400,"联系地址不能为空"); }
        if (StringUtils.isEmpty(contactName)){throw new JayudBizException(400,"联系人不能为空"); }
        if (StringUtils.isEmpty(tel)){throw new JayudBizException(400,"联系电话不能为空"); }
    }

}
