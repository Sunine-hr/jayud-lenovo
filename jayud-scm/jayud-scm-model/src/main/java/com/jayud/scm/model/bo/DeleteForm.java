package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeleteForm {

    @ApiModelProperty(value = "删除记录的ID集合,逗号分割", required = true)
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;

    @ApiModelProperty(value = "删除人姓名", required = true)
    private String name;

    @ApiModelProperty(value = "删除人id")
    private Long id;

    @ApiModelProperty(value = "删除人id")
    private LocalDateTime deleteTime;

    @ApiModelProperty(value = "与前端约定所删除的表")
    private Integer key;

    @ApiModelProperty(value = "表名", required = true)
    private String table;
}
