package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 入库操作记录
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class InGoodsOperationRecordFormVO {

    @ApiModelProperty(value = "入仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "入库件数")
    private Integer number;

    @ApiModelProperty(value = "pcs")
    private Integer pcs;

    @ApiModelProperty(value = "入库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "现有库存")
    private Integer nowNumber;

    @ApiModelProperty(value = "现有pcs")
    private Integer nowPcs;

    @ApiModelProperty(value = "存仓时长")
    private String storageTime;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "库位")
    private String kuCode;
}
