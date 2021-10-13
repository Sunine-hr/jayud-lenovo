package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@ApiModel(value = "MaintenanceManagement对象", description = "维修管理")
public class QueryMaintenanceManagementForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配件名称")
    private String fittingName;

    @ApiModelProperty(value = "车牌")
    private String plateNumber;

    @ApiModelProperty(value = "状态(0:禁用 1:启用, 2:删除)")
    private Integer status;

}
