package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsOutboundOrderInfo;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ciro
 * @date 2021/12/23 11:25
 * @description: 出库单VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单VO", description="出库单VO")
public class WmsOutboundOrderInfoVO extends WmsOutboundOrderInfo {

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
    List<WmsOutboundOrderInfoToMaterialVO> thisMaterialList;

    @ApiModelProperty(value = "本次存在已分配物料信息")
    List<WmsOutboundOrderInfoToDistributionMaterialVO> thisDistributionMaterialList;

    private List<String> orgIds;

    private Boolean isCharge;

    private List<String> owerIdList;

    private List<String> warehouseIdList;

    @ApiModelProperty(value = "出库单号对象")
    private List<String> orderNumberList;

    @ApiModelProperty(value = "出库时间")
    private List<String> theDeliveryTime;

    @ApiModelProperty(value = "附加服务")
    private List<WmsOutboundOrderInfoToService> serviceList;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "作业人员id集合")
    private List<Long> operatorsIds;

    @ApiModelProperty(value = "年月：yyyy-mm")
    private String yearMonth;

}
