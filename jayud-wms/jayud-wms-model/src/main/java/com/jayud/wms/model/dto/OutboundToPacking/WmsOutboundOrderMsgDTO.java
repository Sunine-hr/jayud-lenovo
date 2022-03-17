package com.jayud.wms.model.dto.OutboundToPacking;

import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/10 17:49
 * @description:
 */
@Data
public class WmsOutboundOrderMsgDTO {


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
     * 出库单最终状态
     */
    private Integer orderStatus;

    /**
     * 物料对象集合
     */
    List<WmsOutboundOrderToMaterialDTO> materialList;

}
