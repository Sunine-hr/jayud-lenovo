package com.jayud.finance.po;

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
 * 应收出账单详情
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderReceivableBillDetail对象", description="")
public class OrderReceivableBillDetail extends Model<OrderReceivableBillDetail> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应收对账单详情ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "开始核算期")
    private LocalDateTime beginAccountTerm;

    @ApiModelProperty(value = "结束核算期")
    private LocalDateTime endAccountTerm;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "账单编号维度的本币金额")
    private BigDecimal localAmount;

    @ApiModelProperty(value = "应收费用ID")
    private Long costId;

    @ApiModelProperty(value = "建单日期")
    private LocalDateTime createdOrderTime;

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "大陆车牌号，HK车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车型 如：3T")
    private String vehicleSize;

    @ApiModelProperty(value = "件数")
    private Integer pieceNum;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "生成账单人")
    private String makeUser;

    @ApiModelProperty(value = "生成账单时间")
    private LocalDateTime makeTime;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "开票状态")
    private String applyStatus;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "1-有效 0-无效")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
