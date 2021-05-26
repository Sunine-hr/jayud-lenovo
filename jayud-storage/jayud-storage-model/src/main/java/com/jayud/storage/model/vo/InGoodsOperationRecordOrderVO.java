package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单记录入库商品返回值
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class InGoodsOperationRecordOrderVO {

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "件数")
    private Integer number;

    @ApiModelProperty(value = "pcs")
    private Integer pcs;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "现有件数")
    private Integer nowNumber;

    @ApiModelProperty(value = "现有库存pcs")
    private Integer nowPcs;

    @ApiModelProperty(value = "存仓时间")
    private String storageTime;
}
