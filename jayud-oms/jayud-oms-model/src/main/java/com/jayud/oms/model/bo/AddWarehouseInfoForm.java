package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 仓库信息表
 * </p>
 *
 * @author
 * @since 2020-11-05
 */
@Data
public class AddWarehouseInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "中转仓库代码")
    private String warehouseCode;

    @ApiModelProperty(value = "中转仓库名称")
    @NotEmpty(message = "warehouseName is required")
    private String warehouseName;

    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "contacts is required")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "contactNumber is required")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    @NotEmpty(message = "address is required")
    private String address;

//    @ApiModelProperty(value = "省")
//    @NotEmpty(message = "province is required")
//    private Long province;
//
//    @ApiModelProperty(value = "市")
//    @NotEmpty(message = "city is required")
//    private Long city;
//
//    @ApiModelProperty(value = "区")
//    @NotEmpty(message = "area is required")
//    private Long area;


    @ApiModelProperty(value = "省")
    private Long stateCode;

    @ApiModelProperty(value = "市")
    private Long cityCode;

    @ApiModelProperty(value = "区")
    private Long areaCode;


    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "是否虚拟仓",required = true)
    @NotNull(message = "isVirtual is required")
    private Boolean isVirtual;
}
