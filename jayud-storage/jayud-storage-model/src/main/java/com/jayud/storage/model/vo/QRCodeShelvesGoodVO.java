package com.jayud.storage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * 库位二维码信息
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class QRCodeShelvesGoodVO {

    @ApiModelProperty(value = "库位名称")
    private String kuCode;

    @ApiModelProperty(value = "库位商品信息")
    private List<QRCodeLocationGoodVO> qrCodeLocationGoodVOS;

}
