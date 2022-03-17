package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 客户信息
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "WmsCustomerInfo对象", description = "客户信息")
public class WmsCustomerInfo  extends SysBaseEntity {



    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户类型id")
    private Long customerTypeId;


    @ApiModelProperty(value = "联系人")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;


    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "是否可用，1是，0否")
    private Boolean status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;



}
