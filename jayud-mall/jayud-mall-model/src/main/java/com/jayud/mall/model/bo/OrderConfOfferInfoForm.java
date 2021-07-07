package com.jayud.mall.model.bo;

import com.jayud.mall.model.vo.OfferInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderConfOfferInfoForm {

    @ApiModelProperty(value = "配载id(order_conf id)")
    @NotNull(message = "配载id不能为空")
    private Long confId;

    @ApiModelProperty(value = "报价信息 list")
    @NotNull(message = "报价list不能为空")
    private List<OfferInfoVO> offerInfoList;
}
