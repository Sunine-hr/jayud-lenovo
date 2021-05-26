package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 出库操作记录
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class OutGoodsOperationRecordFormVO {

    @ApiModelProperty(value = "出仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "出库件数")
    private Integer number;

    @ApiModelProperty(value = "出库pcs")
    private Integer pcs;

    @ApiModelProperty(value = "出库时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "存仓时长")
    private String storageTime;

    @ApiModelProperty(value = "库位id")
    private String kuCode;

    @ApiModelProperty(value = "入库商品id")
    private Long inGoodId;


}
