package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "客户审核")
@Data
public class CustomerAuditForm {

    @ApiModelProperty(value = "主键id，自增", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "审核状态(0待审核1审核通过2审核不通过，默认为0）", position = 2)
    @JSONField(ordinal = 2)
    private Integer auditStatus;

    @ApiModelProperty(value = "业务员Id，system_user.id", position = 3)
    @JSONField(ordinal = 3)
    private Integer salesmanId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id),多个用逗号分隔", position = 4)
    @JSONField(ordinal = 4)
    private List<Long> operationTeamId;

    @ApiModelProperty(value = "备注", position = 5)
    @JSONField(ordinal = 5)
    private String remark;

}
