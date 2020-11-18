package com.jayud.finance.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 开票核销
 */
@Data
public class MakeInvoiceForm {

    @ApiModelProperty(value = "开票ID")
    private Long id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票时间")
    private LocalDateTime makeTime;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "附件")
    private String fileUrl;

    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList;


}
