package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 移库操作记录
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class RelocationGoodsOperationRecordFormVO {

    @ApiModelProperty(value = "移库单号")
    private String orderNO;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "移库件数")
    private Integer number;

    @ApiModelProperty(value = "移库时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "存仓时长")
    private String storageTime;
}
