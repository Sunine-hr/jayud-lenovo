package com.jayud.auth.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class QueryCommonConfigForm extends BasePageForm {

    @ApiModelProperty(value = "SQL代码")
    @NotBlank(message = "SQL代码，不能为空")
    private String sqlCode;

    @ApiModelProperty(value = "菜单或按钮code")
//    @NotBlank(message = "菜单或按钮code不为空")
    private String actionCode;

    @ApiModelProperty(value = "条件参数{k-v},键值对")
    private Map<String, Object> condPara;

    @ApiModelProperty(value = "where条件(and ...)")
    private String sqlWhereCondition;
}
