package com.jayud.wms.model.vo;

import com.jayud.common.utils.ListUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ciro
 * @date 2022/1/25 16:31
 * @description: 出库单编码对象VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单编码对象VO", description="出库单编码对象VO")
public class OutboundOrderNumberVO {

    @ApiModelProperty(value = "出库单编号")
    List<String> orderNumberList;

    @ApiModelProperty(value = "是否波次")
    Boolean isWave;

    @ApiModelProperty(value = "拣货下架详细号")
    String packingDetailNumber;

    @ApiModelProperty(value = "编号")
    String orderNumber;

    @ApiModelProperty(value = "任务明细号对象")
    List<String> detailNumberList;

    @ApiModelProperty(value = "是否整个出库单")
    Boolean isAllOrder;

    @ApiModelProperty(value = "是否整个完成继续")
    Boolean isAllContinue;

    @ApiModelProperty(value = "是否整个流程结束")
    Boolean isEnd;

    @ApiModelProperty(value = "是否自动")
    Boolean isAuto;

    @ApiModelProperty(value = "是否拣货下架结束")
    Boolean isEndPacking;

    public List<String> getOrderNumberList(){
        return ListUtils.removeSame(this.orderNumberList);
    }



}

