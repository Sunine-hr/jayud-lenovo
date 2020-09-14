package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DeleteForm {

    @ApiModelProperty(value = "删除记录的ID集合,逗号分割", required = true)
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;
}
