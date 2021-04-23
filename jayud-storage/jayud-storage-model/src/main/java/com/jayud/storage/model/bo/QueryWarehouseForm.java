package com.jayud.storage.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryWarehouseForm extends BasePageForm {

    //仓库名称、仓库代码、创建人、创建日期
    @ApiModelProperty(value = "仓库名称")
    private String name;

    @ApiModelProperty(value = "仓库代码")
    private String code;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
