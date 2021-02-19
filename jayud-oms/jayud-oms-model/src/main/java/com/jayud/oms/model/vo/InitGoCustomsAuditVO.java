package com.jayud.oms.model.vo;

import com.jayud.common.enums.OrderAttachmentTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.OrderAttachment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InitGoCustomsAuditVO {

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "附件")
    private String fileStr;

    @ApiModelProperty(value = "附件名称")
    private String fileNameStr;

    @ApiModelProperty(value = "商品信息")
    private String goodsInfo;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "报关六联单号附件")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "舱单附件")
    private List<FileView> manifestAttachment = new ArrayList<>();

    @ApiModelProperty(value = "报关单附件")
    private List<FileView> customsOrderAttachment = new ArrayList<>();

    public void distributeFiles(List<OrderAttachment> orderAttachments,String url){
        for (OrderAttachment orderAttachment : orderAttachments) {
            List<FileView> fileViews = StringUtils.getFileViews(orderAttachment.getFilePath(), orderAttachment.getFileName(), url);
            if (OrderAttachmentTypeEnum.SIX_SHEET_ATTACHMENT.getDesc().equals(orderAttachment.getRemarks())) {
                this.encodePics=fileViews;
            }
            if (OrderAttachmentTypeEnum.MANIFEST_ATTACHMENT.getDesc().equals(orderAttachment.getRemarks())) {
                this.manifestAttachment=fileViews;
            }
            if (OrderAttachmentTypeEnum.CUSTOMS_ATTACHMENT.getDesc().equals(orderAttachment.getRemarks())) {
                this.customsOrderAttachment=fileViews;
            }
        }
    }

}
