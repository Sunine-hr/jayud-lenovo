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
 * 油卡管理
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OilCardManagement对象", description="油卡管理")
public class AddOilCardManagementForm extends Model<AddOilCardManagementForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "油卡卡号")
    @NotBlank(message = "油卡卡号不能为空")
    private String oilCardNum;

    @ApiModelProperty(value = "油卡名称")
    @NotBlank(message = "油卡名称不能为空")
    private String oilName;

    @ApiModelProperty(value = "领用人id(司机id)")
    private Long driverId;

    @ApiModelProperty(value = "当前使用车辆")
    private Long vehicleId;

    @ApiModelProperty(value = "当前余额(元)")
    @NotNull(message = "余额不能为空")
    private BigDecimal balance;

    @ApiModelProperty(value = "油卡类型(1:充值卡,2:共享卡)")
    private Integer oilType;

    @ApiModelProperty(value = "油卡密码")
    @NotBlank(message = "油卡密码不能为空")
    private String oilPwd;

    @ApiModelProperty(value = "油卡状态(1:使用中,2:闲置中)")
    @NotNull(message = "油卡状态不能为空")
    private Integer oilStatus;

    @ApiModelProperty(value = "充值方式(1:微信,2:支付宝,3:现金)")
    private Integer rechargeType;

    @ApiModelProperty(value = "状态（0:禁用 1:启用）")
    private Integer status;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "归还日期")
    private LocalDateTime returnDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
