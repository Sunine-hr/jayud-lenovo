package com.jayud.tms.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OprStatusForm {

    @ApiModelProperty(value = "主订单号,通关前审核时需，其他场景忽略")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单ID",required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单ID",required = true)
    private Long orderId;

    @ApiModelProperty(value = "状态码")
    private String status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件")
    private String statusPic;

    @ApiModelProperty(value = "附件名称")
    private String statusPicName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "通关时间")
    private String goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private String preGoCustomsTime;

    @ApiModelProperty(value = "过磅数")
    private Double carWeighNum;

    @ApiModelProperty(value = "操作指令,cmd = extCustomsRelease外部报关放行,confirmOrder确认接单,carTakeGoods车辆提货,carWeigh车辆过磅, " +
            "goCustomsAudit通过前审核，goCustomsCheck通关前复核,carGoCustoms车辆通关,hkClearCustoms香港清关,carEnterWarehouse车辆入仓," +
            "carOutWarehouse车辆出仓,carSend车辆派送,confirmSignIn确认签收",required = true)
    private String cmd;

}
