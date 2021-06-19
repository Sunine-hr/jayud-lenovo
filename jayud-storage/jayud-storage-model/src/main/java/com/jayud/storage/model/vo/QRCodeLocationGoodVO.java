package com.jayud.storage.model.vo;

import com.jayud.storage.model.po.InGoodsOperationRecord;
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
public class QRCodeLocationGoodVO {

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "件数")
    private Integer number;

}
