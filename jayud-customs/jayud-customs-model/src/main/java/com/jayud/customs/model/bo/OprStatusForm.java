package com.jayud.customs.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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

    @ApiModelProperty(value = "操作人",required = true)
    @NotNull(message = "操作人不能为空")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间",required = true)
    @NotNull(message = "操作时间不能为空")
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

    @ApiModelProperty(value = "操作指令,cmd = confirmOrder接单 or exceptionOrder异常单 or auditFail审核不通过 or orderList订单列表 or " +
            "issueOrder打单 or toCheck复核 or declare申报 or releaseConfirm放行确认 or auditFailEdit 审核不通过的编辑" +
            "or goCustomsSuccess 通关完成 or customsCheck 通关查验 or customsExcep通关其他异常")
    private String cmd;

}
