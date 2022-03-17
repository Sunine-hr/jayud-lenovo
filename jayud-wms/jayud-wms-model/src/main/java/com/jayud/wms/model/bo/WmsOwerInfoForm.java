package com.jayud.wms.model.bo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * WmsOwerInfo 实体类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Data
public class WmsOwerInfoForm extends SysBaseEntity {


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

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "备用字段3")
    private String columnThree;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "是否可用，1是，0否")
    private Boolean isOn;

    @ApiModelProperty(value = "上架策略id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long shelfStrategyId;

    @ApiModelProperty(value = "分配策略id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long allocationStrategyId;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


    @ApiModelProperty(value = "所属仓库集合")
    private List<Long> warehouseList;


    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;



}
