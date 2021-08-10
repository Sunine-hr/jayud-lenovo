package com.jayud.scm.model.bo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionForm {

    @ApiModelProperty(value = "权限Code", required = true)
    private String actionCode;

    @ApiModelProperty(value = "与前端约定所审核的表")
    private Integer key;

    @ApiModelProperty(value = "表名")
    private String table;

    @ApiModelProperty(value = "审核或反审的记录id")
    private Integer id;

    @ApiModelProperty(value = "审核 1 反审2 非审核 0", required = true)
    private Integer type;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "是否是客户审核")
    private Boolean customerAudit;

}
