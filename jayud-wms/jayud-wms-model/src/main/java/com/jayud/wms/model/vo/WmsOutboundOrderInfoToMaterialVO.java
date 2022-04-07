package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ciro
 * @date 2021/12/23 11:27
 * @description: 出库单-物料信息VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单-物料信息VO", description="出库单-物料信息VO")
public class WmsOutboundOrderInfoToMaterialVO extends WmsOutboundOrderInfoToMaterial {

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

    @ApiModelProperty(value = "分配库位、数量数据")
    private List<InventoryDetail> detailList;

    @ApiModelProperty(value = "出库单号对象")
    private List<String> orderNumberList;

    @ApiModelProperty(value = "物料对象")
    private Map<String,WmsOutboundOrderInfoToMaterialVO> materialMap;

    /**
     * 是否添加分配
     */
    @ApiModelProperty(value = "是否添加分配")
    private Boolean isAddDistribution;

    @ApiModelProperty(value = "出库单状态文本")
    private String statusType_text;

    @ApiModelProperty(value = "是否有图片")
    private Boolean isHavePic;

}
