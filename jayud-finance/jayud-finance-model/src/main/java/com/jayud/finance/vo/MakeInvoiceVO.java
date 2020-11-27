package com.jayud.finance.vo;

import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 开票核销列表
 */
@Data
public class MakeInvoiceVO  {

    @ApiModelProperty(value = "开票/付款ID")
    private Long id;

    @ApiModelProperty(value = "票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票时间")
    private LocalDateTime makeTime;

    @ApiModelProperty(value = "开票时间")
    private String makeTimeStr;

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

    @ApiModelProperty(value = "是否作废 1-没作废 0-作废")
    private String status;

    public String getMakeTimeStr() {
        if(this.makeTime != null){
            return DateUtils.getLocalToStr(this.makeTime);
        }
        return "";
    }


}
