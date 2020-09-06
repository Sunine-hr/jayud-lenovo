package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 删除法人主体
 * @author chuanmei
 */
@Data
public class DelLegalEntityForm {

    @ApiModelProperty(value = "法人主体ID", required = true)
    @NotEmpty(message = "法人主体ID不能为空")
    private List<Long> ids;
}
