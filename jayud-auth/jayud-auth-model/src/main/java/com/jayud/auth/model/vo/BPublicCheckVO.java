package com.jayud.auth.model.vo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BPublicCheck 实体类
 *
 * @author jayud
 * @since 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="审核记录表对象", description="审核记录表")
public class BPublicCheckVO extends SysBaseEntity {


    @ApiModelProperty(value = "单据编号")
    private String sheetCode;

    @ApiModelProperty(value = "操作,1审核，0反审")
    private Integer checkFlag;

    @ApiModelProperty(value = "操作,1审核，0反审")
    private String checkFlagRemark;

    @ApiModelProperty(value = "审核是否通过")
    private Boolean isCheck;

    @ApiModelProperty(value = "审核是否通过")
    private String isCheckRemark;

    @ApiModelProperty(value = "耗时")
    private String timeConsuming;

    @ApiModelProperty(value = "记录ID")
    private Long recordId;

    @ApiModelProperty(value = "需要审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前审核级别")
    private Integer fStep;

    @ApiModelProperty(value = "当前审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核人ID")
    private Long fCheckId;

    @ApiModelProperty(value = "审核人名称")
    private String fCheckName;


}
