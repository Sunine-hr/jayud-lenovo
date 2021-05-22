package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 库存表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-29
 */
@Data
public class StockRecordVO {

    @ApiModelProperty(value = "商品名称")
    private String goodName;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "可用库存")
    private Integer availableStock;

    @ApiModelProperty(value = "锁定库存")
    private Integer lockStock;

    @ApiModelProperty(value = "入库记录")
    private List<InGoodsOperationRecordFormVO> inGoodsOperationRecordFormVOS;

    @ApiModelProperty(value = "出库记录")
    private List<OutGoodsOperationRecordFormVO> outGoodsOperationRecordFormVOS;

    @ApiModelProperty(value = "移库记录")
    private List<RelocationGoodsOperationRecordFormVO> relocationGoodsOperationRecordFormVOS;

    @ApiModelProperty(value = "调整上架记录")
    private List<AdjustShelfRecordFormVO> adjustShelfRecordFormVOS;

    @ApiModelProperty(value = "调整下架记录")
    private List<AdjustOffShelfRecordFormVO> adjustOffShelfRecordFormVOS;

}