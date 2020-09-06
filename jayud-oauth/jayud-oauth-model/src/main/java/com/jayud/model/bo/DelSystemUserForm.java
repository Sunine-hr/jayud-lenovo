package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 删除员工
 * @author chuanmei
 */
@Data
public class DelSystemUserForm {

    @ApiModelProperty(value = "员工ID", required = true)
    @NotEmpty(message = "员工ID不能为空")
    private List<Long> userIds;
}
