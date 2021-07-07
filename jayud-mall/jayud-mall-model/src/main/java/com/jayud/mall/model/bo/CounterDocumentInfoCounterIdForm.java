package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CounterDocumentInfoCounterIdForm {

    @ApiModelProperty(value = "柜子id(ocean_counter id)")
    private Long counterId;

}
