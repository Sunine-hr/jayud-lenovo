package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryServiceGroupForm extends BasePageForm{

    @ApiModelProperty(value = "代码")
    private String idCode;

    @ApiModelProperty(value = "名称")
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField("`status`")
    private String status;
}
