package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * WmsOwerInfo 实体类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WmsOwerInfo对象", description="货主信息")
public class WmsOwerInfo extends SysBaseEntity {


    @ApiModelProperty(value = "货主编号")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "联系人")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "备用字段3")
    private String columnThree;

    @ApiModelProperty(value = "是否可用，1是，0否")
    private Boolean isOn;

    @ApiModelProperty(value = "上架策略id")
    private Long shelfStrategyId;


    @ApiModelProperty(value = "分配策略id")
    private Long allocationStrategyId;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;

//
//    @ApiModelProperty(value = "所属仓库集合")
//    private List<Long> warehouseList;

    public   final static  String  ENCODING_OWER  ="SZ";

    @TableField(exist = false)
    private List<String> owerIdList;

}
