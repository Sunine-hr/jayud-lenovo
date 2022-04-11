package com.jayud.wms.model.vo.LargeScreen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2022/4/11 11:02
 * @description: 工单数量
 */
@Data
@ApiModel(value = "工单数量", description = "工单数量")
public class OrderCountVO {

    @ApiModelProperty(value = "入库通知单完成数量")
    private int inBoundNoticeOrderFinishCount;

    @ApiModelProperty(value = "入库通知单未完成数量")
    private int inBoundNoticeOrderUnFinishCount;

    @ApiModelProperty(value = "入库单完成数量")
    private int inBoundOrderFinishCount;

    @ApiModelProperty(value = "入库单未完成数量")
    private int inBoundOrderUnFinishCount;

    @ApiModelProperty(value = "入库质检单完成数量")
    private int inBoundQualityFinishCount;

    @ApiModelProperty(value = "入库质检单未完成数量")
    private int inBoundQualityUnFinishCount;

    @ApiModelProperty(value = "出库通知完成单数量")
    private int outoundNoticeOrderFinishCount;

    @ApiModelProperty(value = "出库通知未完成单数量")
    private int outoundNoticeOrderUnFinishCount;

    @ApiModelProperty(value = "出库单完成数量")
    private int outBoundOrderFinishCount;

    @ApiModelProperty(value = "出库单未完成数量")
    private int outBoundOrderUnFinishCount;

    @ApiModelProperty(value = "出库发运复核单完成数量")
    private int outBoundShippingReviewFinishCount;

    @ApiModelProperty(value = "出库发运复核单未完成数量")
    private int outBoundShippingReviewUnFinishCount;


}
