package com.jayud.finance.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 开票核销
 */
@Data
public class MakeInvoiceForm {

    @ApiModelProperty(value = "开票ID,有的话必传")
    private Long id;

    @ApiModelProperty(value = "账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "票号",required = true)
    @NotEmpty(message = "invoiceNo is required")
    private String invoiceNo;

    @ApiModelProperty(value = "开票时间",required = true)
    @NotEmpty(message = "makeTimeStr is required")
    private String makeTimeStr;

    @ApiModelProperty(value = "发票类型",required = true)
    @NotEmpty(message = "invoiceType is required")
    private String invoiceType;

    @ApiModelProperty(value = "金额",required = true)
    @NotNull(message = "money is required")
    private BigDecimal money;

    @ApiModelProperty(value = "附件")
    private String fileUrl;

    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();


}
