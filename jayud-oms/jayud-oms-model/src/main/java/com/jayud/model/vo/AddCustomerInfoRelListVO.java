package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AddCustomerInfoRelListVO {

    @ApiModelProperty(value = "接单部门集合")
    private List<Map<Long,String>> departments;

    @ApiModelProperty(value = "业务员集合")
    private List<Map<Long,String>> yws;

    @ApiModelProperty(value = "接单客服集合")
    private List<Map<Long,String>> kfs;
}
