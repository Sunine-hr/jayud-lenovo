package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsOutboundOrderInfoToDistributionMaterial;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ciro
 * @date 2021/12/23 16:45
 * @description: 出库单--物料信息VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单--物料信息VO", description="出库单--物料信息VO")
public class WmsOutboundOrderInfoToDistributionMaterialVO extends WmsOutboundOrderInfoToDistributionMaterial {
}
