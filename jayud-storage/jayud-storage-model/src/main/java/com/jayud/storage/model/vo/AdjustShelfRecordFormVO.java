package com.jayud.storage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 调整上架操作记录
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class AdjustShelfRecordFormVO {

    @ApiModelProperty(value = "调整上架订单单号")
    private String orderNO;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "上架数量件数")
    private Integer number;

    @ApiModelProperty(value = "上架时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "存仓时长")
    private String storageTime;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
