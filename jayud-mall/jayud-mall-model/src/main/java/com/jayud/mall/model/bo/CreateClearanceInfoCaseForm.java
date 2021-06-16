package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateClearanceInfoCaseForm {

    @ApiModelProperty(value = "提单对应清关信息id(bill_clearance_info id)")
    @NotNull(message = "提单对应清关信息id,不能为空")
    private Long bId;

    @ApiModelProperty(value = "箱号id(order_case id) list")
    @NotNull(message = "箱号id,不能为空")
    private List<Long> caseIds;

}
