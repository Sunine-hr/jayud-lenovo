package com.jayud.storage.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 * 二维码记录信息
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class QRCodeInformationVO {

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "入仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "入仓时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "货物信息")
    private String goodInfo;

    @ApiModelProperty(value = "总板数")
    private Integer totalAmount;

    @ApiModelProperty(value = "总件数")
    private Integer totalJAmount;

    @ApiModelProperty(value = "总pcs")
    private Integer totalPCS;

    @ApiModelProperty(value = "货物信息")
    private List<InGoodsOperationRecord> goodInfos;
}
