package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateCustomsInfoCaseForm {

    @ApiModelProperty(value = "提单对应报关信息id(bill_customs_info id)")
    @NotNull(message = "提单对应报关信息id,不能为空")
    @JsonProperty(value = "bId")
    private Long bId;

    @ApiModelProperty(value = "箱号id(order_case id) list")
    @NotNull(message = "箱号id,不能为空")
    private List<Long> caseIds;

}
