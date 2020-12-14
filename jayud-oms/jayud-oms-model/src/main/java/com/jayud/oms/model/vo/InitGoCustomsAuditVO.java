package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InitGoCustomsAuditVO {

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "附件")
    private String fileStr;

    @ApiModelProperty(value = "附件名称")
    private String fileNameStr;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

}
