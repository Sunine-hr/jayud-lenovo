package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 油卡管理
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OilCardManagement对象", description = "油卡管理")
public class OilCardManagementVO extends Model<OilCardManagementVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "油卡卡号")
    private String oilCardNum;

    @ApiModelProperty(value = "油卡名称")
    private String oilName;

    @ApiModelProperty(value = "领用人id(司机id)")
    private Long driverId;

    @ApiModelProperty(value = "领用人")
    private String driverName;

    @ApiModelProperty(value = "使用车辆id")
    private Long vehicleId;

    @ApiModelProperty(value = "使用车辆")
    private String vehicleName;

    @ApiModelProperty(value = "当前余额(元)")
    private BigDecimal balance;

    @ApiModelProperty(value = "油卡类型(1:充值卡,2:共享卡)")
    private Integer oilType;

    @ApiModelProperty(value = "油卡类型(1:充值卡,2:共享卡)")
    private Integer oilTypeDesc;

    @ApiModelProperty(value = "油卡密码")
    private String oilPwd;

    @ApiModelProperty(value = "油卡状态(1:使用中,2:闲置中)")
    private Integer oilStatus;

    @ApiModelProperty(value = "油卡状态(1:使用中,2:闲置中)")
    private String oilStatusDesc;

    @ApiModelProperty(value = "充值方式(1:微信,2:支付宝,3:现金)")
    private Integer rechargeType;

    @ApiModelProperty(value = "充值方式(1:微信,2:支付宝,3:现金)")
    private String rechargeTypeDesc;

    @ApiModelProperty(value = "状态（0:禁用 1:启用）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "归还日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
