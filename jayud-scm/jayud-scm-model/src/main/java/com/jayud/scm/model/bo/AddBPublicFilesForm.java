package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class AddBPublicFilesForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "附件类型,1:商品库,2:客户主体,3委托订单,4:付款单,5:收款单,6:入库单,7:出库单,8:应收款,9:提验货,10:中港运输")
    private Integer fileModel;

    @ApiModelProperty(value = "业务单据ID")
    private Integer businessId;

    @ApiModelProperty(value = "上传文件类型（水单，证照等）")
    private String fileType;

    @ApiModelProperty(value = "文件内容说明")
    private String fileContext;

    @ApiModelProperty(value = "上传源文件名")
    private String fileName;

    @ApiModelProperty(value = "上传源路径")
    private String filePath;

    @ApiModelProperty(value = "保存文件名")
    private String sFileName;

    @ApiModelProperty(value = "保存路径")
    private String sFilePath;

    @ApiModelProperty(value = "文件大小")
    private BigDecimal contentLength;

    @ApiModelProperty(value = "文件类型")
    private String contentType;

    @ApiModelProperty(value = "上传电脑名称")
    private String inputComputer;

    @ApiModelProperty(value = "上传电脑IP")
    private String inputIp;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "上传附件")
    private FileView fileView;

}
