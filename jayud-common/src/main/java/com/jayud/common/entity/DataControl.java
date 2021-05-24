package com.jayud.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DataControl {
    @ApiModelProperty(value = "当前用户账户类型(1-用户 2-客户 3-供应商)")
    private String accountType;

    @ApiModelProperty(value = "当前用户账户类型(1-用户 2-客户 3-供应商)")
    private List<Long> companyIds;
}
