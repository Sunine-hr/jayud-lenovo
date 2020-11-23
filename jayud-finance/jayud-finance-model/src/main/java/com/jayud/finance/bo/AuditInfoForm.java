package com.jayud.finance.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuditInfoForm {

    @ApiModelProperty(value = "被审核表的ID")
    private Long extId;

    //因为账单的审核都是以账单编号为维度进行审核的，而表是最细的费用类型为维度
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

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews = new ArrayList<>();


}
