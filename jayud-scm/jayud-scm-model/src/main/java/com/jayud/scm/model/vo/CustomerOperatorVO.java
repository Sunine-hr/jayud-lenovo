package com.jayud.scm.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 客户操作人员信息
 */
@Data
public class CustomerOperatorVO {

    //`商务员`、`业务员`、`客户下单人`

    @ApiModelProperty(value = "商务员list")
    private List<CustomerMaintenanceSetupVO> followerList;

    @ApiModelProperty(value = "业务员list")
    private List<CustomerMaintenanceSetupVO> fsalesList;

    @ApiModelProperty(value = "客户下单人list")
    private List<CustomerRelationerVO> buyerList;
}
