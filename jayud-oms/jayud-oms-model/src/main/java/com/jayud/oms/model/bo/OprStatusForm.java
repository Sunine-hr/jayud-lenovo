package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderAttachmentTypeEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.po.OrderAttachment;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty(value = "业务类型(0:空运),(1,纯报关),ZGYS(2,中港运输) BusinessTypeEnum")
    private Integer businessType;

    @ApiModelProperty(value = "外部报关六联单号附件")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "外部舱单附件")
    private List<FileView> manifestAttachment = new ArrayList<>();

    @ApiModelProperty(value = "报关单附件")
    private List<FileView> customsOrderAttachment = new ArrayList<>();

    /**
     * 外部报关参数校验
     */
    public void checkExternalCustomsDeclarationParam(){
        if (this.mainOrderId == null || StringUtil.isNullOrEmpty(this.operatorUser) ||
                StringUtil.isNullOrEmpty(this.encode)) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }
        //六联单号必须为13位的纯数字
        String encode = this.encode;
        if (!(encode.matches("[0-9]{1,}") && encode.length() == 13)) {
            throw new JayudBizException(ResultEnum.ENCODE_PURE_NUMBERS);
        }
        if (this.manifestAttachment.size()==0) {
            throw new JayudBizException(400,"上传舱单文件");
        }
        if (this.customsOrderAttachment.size()==0) {
            throw new JayudBizException(400,"上传报关文件");
        }
    }

    /**
     * 组合附件
     */
    public Map<String, List<FileView>> assemblyAttachment() {
        Map<String, List<FileView>> map = new HashMap<>(3);
        map.put(OrderAttachmentTypeEnum.SIX_SHEET_ATTACHMENT.getDesc(), encodePics);
        map.put(OrderAttachmentTypeEnum.MANIFEST_ATTACHMENT.getDesc(), manifestAttachment);
        map.put(OrderAttachmentTypeEnum.CUSTOMS_ATTACHMENT.getDesc(), customsOrderAttachment);
        return map;
    }
}
