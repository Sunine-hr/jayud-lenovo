package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 物料类型信息
 * </p>
 */
@Data
public class MaterialTypeInfoForm implements Serializable {

    private Long id;// ID

    @ApiModelProperty(value = "父节点Id")
    private Long parentId;

    @ApiModelProperty(value = "物料类型编码")
    private String materialTypeCode;

    @ApiModelProperty(value = "物料类型名称")
    private String materialTypeName;

    @ApiModelProperty(value = "排序")
    private Long order;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "创建人名称")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改人名称")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
