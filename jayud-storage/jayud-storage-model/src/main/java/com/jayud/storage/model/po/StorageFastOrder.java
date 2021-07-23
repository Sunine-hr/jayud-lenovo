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
import lombok.experimental.Accessors;

/**
 * <p>
 * 快进快出订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-06-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="StorageFastOrder对象", description="快进快出订单表")
public class StorageFastOrder extends Model<StorageFastOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态()")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "入库车牌号")
    private String inPlateNumber;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "预计到达时间")
    private LocalDateTime estimatedArrivalTime;

    @ApiModelProperty(value = "预计出库时间")
    private LocalDateTime expectedDeliveryTime;

    @ApiModelProperty(value = "出库车牌号")
    private String outPlateNumber;

    @ApiModelProperty(value = "出仓号")
    private String outWarehouseNumber;

    @ApiModelProperty(value = "是否入库（1为入库，0为不入库）")
    private Integer isWarehouse;

    @ApiModelProperty(value = "是否需要录入费用(0:false,1:true)")
    private Boolean needInputCost;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
