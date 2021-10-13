package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 维修管理
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MaintenanceManagement对象", description = "维修管理")
public class AddMaintenanceManagementForm extends Model<AddMaintenanceManagementForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "配件名称")
    @NotBlank(message = "配件名称不能为空")
    private String fittingName;

    @ApiModelProperty(value = "车牌")
    @NotNull(message = "请选择车牌")
    private String plateNumber;

    @ApiModelProperty(value = "维修金额")
    @NotNull(message = "请填写维修金额")
    private BigDecimal repairAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
