package com.jayud.oms.model.po;

import java.math.BigDecimal;
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
 * 司机录入费用表(小程序使用)
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DriverEmploymentFee对象", description="司机录入费用表(小程序使用)")
public class DriverEmploymentFee extends Model<DriverEmploymentFee> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "司机录用费用id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "司机id")
    private Long driverId;

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;

    @ApiModelProperty(value = "中港订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单编码")
    private String orderNo;

    @ApiModelProperty(value = "费用代码")
    private String costCode;

    @ApiModelProperty(value = "费用金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "多个文件，用逗号隔开")
    private String fileName;

    @ApiModelProperty(value = "多个文件路径,用逗号隔开")
    private String files;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "状态(0:待提交，1:已提交)")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
