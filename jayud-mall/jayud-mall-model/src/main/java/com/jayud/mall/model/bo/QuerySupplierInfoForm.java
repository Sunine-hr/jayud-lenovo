package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查询供应商表单")
public class QuerySupplierInfoForm extends BasePageForm {

    @ApiModelProperty(value = "公司名称", position = 1)
    @JSONField(ordinal = 1)
    private String companyName;

    @ApiModelProperty(value = "服务类型List<Long>", position = 2)
    @JSONField(ordinal = 2)
    private List<Long> serviceTypeIds;

    @ApiModelProperty(value = "评级(1-5颗星)", position = 3)
    @JSONField(ordinal = 3)
    private Integer grade;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

    @ApiModelProperty(value = "供应商名称(中)", position = 5)
    @JSONField(ordinal = 4)
    private String supplierChName;

    @ApiModelProperty(value = "供应商名称(英)", position = 6)
    @JSONField(ordinal = 6)
    private String supplierEnName;

    @ApiModelProperty(value = "关键字(供应商代码or供应商名称(中))", position = 7)
    @JSONField(ordinal = 7)
    private String keyword;

}
