package com.jayud.wms.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ciro
 * @date 2021/12/20 14:34
 * @description: 出库通知单VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库通知单VO", description="出库通知单VO")
public class WmsOutboundNoticeOrderInfoVO extends WmsOutboundNoticeOrderInfo {


    @ApiModelProperty(value = "单据类型名称")
    private String documentType_text;

    @ApiModelProperty(value = "订单来源名称")
    private String orderSourceType_text;

    @ApiModelProperty(value = "订单状态名称")
    private String orderStatusType_text;


    @ApiModelProperty(value = "预计出库开始时间")
    private String planDeliveryTime_start;

    @ApiModelProperty(value = "预计出库结束时间")
    private String planDeliveryTime_end;


    @ApiModelProperty(value = "本次存在物料信息")
    List<WmsOutboundNoticeOrderInfoToMaterialVO> thisMaterialList;

    private List<String> orgIds;

    @JsonProperty(value = "isCharge")
    private Boolean isCharge;

    private List<String> owerIdList;

    private List<String> warehouseIdList;

    @JsonProperty(value = "isAuto")
    @ApiModelProperty(value = "是否自动")
    Boolean isAuto;

    @JsonProperty(value = "isEndPacking")
    @ApiModelProperty(value = "是否拣货下架结束")
    Boolean isEndPacking;

    @ApiModelProperty(value = "发货时间")
    private List<String> theDeliveryTime;

}
