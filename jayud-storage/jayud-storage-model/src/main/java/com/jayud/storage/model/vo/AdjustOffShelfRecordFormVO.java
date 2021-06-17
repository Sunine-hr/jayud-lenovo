package com.jayud.storage.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 调整下架操作记录
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class AdjustOffShelfRecordFormVO {

    @ApiModelProperty(value = "调整下架单号")
    private String orderNO;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "下架件数")
    private Integer number;

    @ApiModelProperty(value = "下架时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "存仓时长")
    private String storageTime;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
