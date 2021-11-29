package com.jayud.scm.model.vo;

import com.jayud.scm.model.bo.AddAddressForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 推送至内陆订单的数据
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
public class HubToInlandTransportVO {

    @ApiModelProperty(value = "车次编号")
    private String orderNo;

    @ApiModelProperty(value = "车型")
    private String vehicleType;

    @ApiModelProperty(value = "车型大小")
    private String vehicleSize;

    @ApiModelProperty(value = "提货地址")
    private List<AddAddressForm> takeAdrForms1;

    @ApiModelProperty(value = "送货地址")
    private List<AddAddressForm> takeAdrForms2;

}
