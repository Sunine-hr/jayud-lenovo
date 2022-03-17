package com.jayud.wms.model.dto.OutboundToPacking;

import lombok.Data;

/**
 * @author ciro
 * @date 2022/3/10 17:50
 * @description:    分配后物料
 */
@Data
public class WmsOutboundOrderToDistributeMaterialDTO {

    /**
     *  分配物料id
     */
    private Long distributionMaterialId;

    /**
     *  拣货下架状态
     */
    private Integer packingMaterialStatus;

}
