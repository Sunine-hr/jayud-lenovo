package com.jayud.wms.model.vo;


import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author ciro
 * @date 2021/12/20 14:37
 * @description: 出库通知单-物料信息VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库通知单-物料信息VO", description="出库通知单-物料信息VO")
public class WmsOutboundNoticeOrderInfoToMaterialVO extends WmsOutboundNoticeOrderInfoToMaterial {



    @ApiModelProperty(value = "物料类型id")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料类型编码")
    private String materialTypeCode;

    @ApiModelProperty(value = "物料类型名称")
    private String materialTypeId_text;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;


}
