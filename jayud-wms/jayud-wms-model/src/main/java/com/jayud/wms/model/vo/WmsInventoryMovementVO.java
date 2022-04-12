package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.wms.model.bo.InventoryMovementTaskForm;
import com.jayud.wms.model.enums.InventoryMovementEnum;
import com.jayud.wms.model.enums.ReceiptStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * WmsInventoryMovement 实体类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="库存移动订单表对象", description="库存移动订单表")
public class WmsInventoryMovementVO extends SysBaseEntity {


    @ApiModelProperty(value = "移库单号")
    private String movementCode;

    @ApiModelProperty(value = "订单状态(1:待移库,2:已移库,3：)")
    private Integer status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "移库完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date shiftingParkingTime;


    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "原仓库名称list")
    private String warehouseNameList;

    @ApiModelProperty(value = "订单状态明细 1：待移库：2：已移库，3：待定）")
    private String statusDetails;


    @ApiModelProperty(value = "库存移动任务集合")
    private List<InventoryMovementTaskForm> inventoryMovementTaskForms;



    public void setStatus(Integer status) {
        this.status = status;
        this.statusDetails = InventoryMovementEnum.getDesc(status);
    }


}
