package com.jayud.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LegalEntity对象", description="法人主体表")
@Data
public class LegalEntity {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "主体编号")
    private String legalCode;

    @ApiModelProperty(value = "注册所在地")
    private String rigisAddress;

    @ApiModelProperty(value = "销售部门")
    private Long saleDepartId;

    @ApiModelProperty(value = "审核状态 1-待审核 2-通过 3-拒绝")
    private Long auditStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

}
