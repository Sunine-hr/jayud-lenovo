package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InputSubOrderCustomsVO {

    @ApiModelProperty(value = "子订单ID")
    private Long subOrderId;

    @ApiModelProperty(value = "报关子订单")
    private String orderNo;

    @ApiModelProperty(value = "报关抬头")
    private String title;

    @ApiModelProperty(value = "单双抬头")
    private String isTitle;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews = new ArrayList<>();

}
