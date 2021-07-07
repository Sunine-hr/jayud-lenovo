package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CounterDocumentInfoIdForm {

    @ApiModelProperty(value = "柜子文件id(counter_document_info id)")
    @NotNull(message = "柜子文件id不能为空")
    private Long id;

}
