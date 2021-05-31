package com.jayud.customs.model.po;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Email {
    @ApiModelProperty(value = "发件人")
    private String from;
    @ApiModelProperty(value = "收件人")
    private String to;
    @ApiModelProperty(value = "抄送人")
    private String cc;
    @ApiModelProperty(value = "主题")
    private String subject;
    @ApiModelProperty(value = "内容")
    private String text;
    @ApiModelProperty(value = "附件")
    private List<FileView> fileViews;
}
