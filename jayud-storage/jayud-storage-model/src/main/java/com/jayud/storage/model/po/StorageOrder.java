package com.jayud.storage.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="StorageOrder对象", description="")
public class StorageOrder extends Model<StorageOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "状态(1为有效，2为无效)")
    private Integer status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "出库订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "存仓时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "存仓截止时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "所属月份")
    private String month;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
