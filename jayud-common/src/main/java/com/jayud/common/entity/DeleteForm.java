package com.jayud.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeleteForm {

    @ApiModelProperty(value = "删除id集合")
    private List<Long> ids;



}
