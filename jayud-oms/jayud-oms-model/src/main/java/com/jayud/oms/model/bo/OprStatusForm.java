package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OprStatusForm {

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单ID")
    private Long orderId;

    @ApiModelProperty(value = "状态码")
    private String status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String operatorTime;

    @ApiModelProperty(value = "附件")
    private String statusPic;

    @ApiModelProperty(value = "附件名称")
    private String statusPicName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "委托号")
    private String entrustNo;

    @ApiModelProperty(value = "通关时间")
    private String goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private String preGoCustomsTime;

    @ApiModelProperty(value = "外部报关放行的六联单号")
    private String encode;

    @ApiModelProperty(value = "业务类型(0:空运),(1,纯报关),ZGYS(2,中港运输)")
    private Integer businessType;

    @ApiModelProperty(value = "外部报关六联单号附件")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "外部舱单附件")
    private List<FileView> manifestAttachment = new ArrayList<>();

    @ApiModelProperty(value = "报关单附件")
    private List<FileView> customsOrderAttachment = new ArrayList<>();

}
