package com.jayud.auth.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysPost 实体类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="岗位表对象", description="岗位表")
public class SysPostForm extends SysBaseEntity {


    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "岗位排序")
    private Long postSequence;

    @ApiModelProperty(value = "上级岗位ID")
    private Long parentId;

    @ApiModelProperty(value = "所属部门id")
    private Long departId;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
