package com.jayud.tms.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OprStatusForm {
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

    @ApiModelProperty(value = "操作指令,cmd = extCustomsRelease外部报关放行 ")
    private String cmd;

}
