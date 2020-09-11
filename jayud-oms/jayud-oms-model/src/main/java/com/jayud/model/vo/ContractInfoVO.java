package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class ContractInfoVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同地址")
    private String contractUrl;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型集合")
    private List<Long> businessTypes;

    @ApiModelProperty(value = "法人主体")
    private Long legalEntity;

    @ApiModelProperty(value = "合同起期")
    private String startDate;

    @ApiModelProperty(value = "合同止期")
    private String endDate;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;


}
