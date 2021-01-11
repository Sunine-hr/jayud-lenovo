package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 仓库信息表
 * </p>
 *
 * @author 
 * @since 2020-11-05
 */
@Data
public class WarehouseInfoVO  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "中转仓库代码")
    private String warehouseCode;

    @ApiModelProperty(value = "中转仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "省码")
    private Long stateCode;

    @ApiModelProperty(value = "市码")
    private Long cityCode;

    @ApiModelProperty(value = "区码")
    private Long areaCode;


    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;


    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户名")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否虚拟仓")
    private Boolean isVirtual;


}
