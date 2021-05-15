package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AuditInfoForm {

    @ApiModelProperty(value = "被审核表的ID")
    private Long extId;

    @ApiModelProperty(value = "被审核表唯一标识,比如账单号")
    private String extUniqueFlag;

    @ApiModelProperty(value = "审核类别描述")
    private String auditTypeDesc;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "被审核表的描述")
    private String extDesc;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews = new ArrayList<>();


}
