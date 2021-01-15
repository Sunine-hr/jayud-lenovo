package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CostItemForm {

    @ApiModelProperty(value = "主键", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "辨认费用类型(1应收费用 2应付费用)", position = 2)
    @JSONField(ordinal = 2)
    private String identifying;

    @ApiModelProperty(value = "费用代码", position = 3)
    @JSONField(ordinal = 3)
    private String costCode;

    @ApiModelProperty(value = "费用名称", position = 4)
    @JSONField(ordinal = 4)
    private String costName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @JSONField(ordinal = 5)
    private String status;

    @ApiModelProperty(value = "例外的费用代码", position = 6)
    @JSONField(ordinal = 6)
    private List<String> notCostCodes;

}
