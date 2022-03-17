package com.jayud.wms.model.dto.OutboundToPacking;

import lombok.Data;

/**
 * @author ciro
 * @date 2022/3/10 17:38
 * @description: 出库单-拣货下架
 */
@Data
public class WmsOutboundToPackingDTO {

    /**
     * 出库单id
     */
    private Long orderId;

    /**
     *  出库单编码
     */
    private String orderNumber;

    /**
     *  出库通知单编码
     */
    private String noticeOrderNumber;

    /**
     *  出库单状态
     */
    private Integer orderStatus;

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
     *  分配物料id
     */
    private Long distributionMaterialId;

    /**
     *  拣货下架状态
     */
    private Integer packingMaterialStatus;


}
