package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 上传附件表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BPublicFilesVO {

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "附件类型,1:商品库,2:客户主体,3委托订单,4:付款单,5:收款单,6:入库单,7:出库单,8:应收款,9:提验货,10:中港运输")
    private Integer fileModel;

    @ApiModelProperty(value = "附件类型,1:商品库,2:客户主体,3委托订单,4:付款单,5:收款单,6:入库单,7:出库单,8:应收款,9:提验货,10:中港运输")
    private String fileModelCopy;

    @ApiModelProperty(value = "业务单据ID")
    private Integer businessId;

    @ApiModelProperty(value = "上传文件类型（水单，证照等）")
    private String fileType;

    @ApiModelProperty(value = "内容")
    private List<FileView> fileView;

    @ApiModelProperty(value = "保存文件名")
    @JsonProperty("sFileName")
    private String sFileName;

    @ApiModelProperty(value = "保存路径")
    @JsonProperty("sFilePath")
    private String sFilePath;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "备注")
    private String remark;

}
