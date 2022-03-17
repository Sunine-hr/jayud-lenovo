package com.jayud.wms.model.dto.OutboundToPacking;

import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/10 17:50
 * @description:    物料信息
 */
@Data
public class WmsOutboundOrderToMaterialDTO {

    /**
     *  物料id
     */
    private Long materialId;

    /**
     * 物料类型id
     */
    private Long materialTypeId;

    /**
     * 物料类型编码
     */
    private String materialTypeCode;

    /**
     * 物料类型名称
     */
    private String materialTypeName;


    /**
     *  物料状态
     */
    private Integer materialStatus;

    /**
     * 分配后物料集合
     */
    private List<WmsOutboundOrderToDistributeMaterialDTO> distributeList;

}
