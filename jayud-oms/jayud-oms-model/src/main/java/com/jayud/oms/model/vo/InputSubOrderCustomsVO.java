package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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

    @ApiModelProperty(value = "结算单位code")
    private String unitName;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单时间")
    private String jiedanTimeStr;

    @ApiModelProperty(value = "报关单号，预留字段")
    private String yunCustomsNo;

    @ApiModelProperty(value = "放行状态")
    private String statusDesc;

    @ApiModelProperty(value = "重量，预留字段")
    private BigDecimal weight;

    @ApiModelProperty(value = "件数，预留字段")
    private Integer pieceNumber;

    @ApiModelProperty(value = "识别信息，预留字段")
    private String isRight;

    @ApiModelProperty(value = "委托单号")
    private String entrustNo;

    @ApiModelProperty(value = "监管方式")
    private String supervisionMode;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "法人名称")
    private String legalName;

    @ApiModelProperty(value = "法人名称id")
    private Long legalEntityId;

    public void copyOperationInfo() {
        this.subOrderId = null;
        this.fileViews = null;
        this.yunCustomsNo = null;
        this.jiedanTimeStr=null;

    }
}
