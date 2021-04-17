package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
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
 * 供应商服务费用
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SupplierCost对象", description="供应商服务费用")
public class SupplierCost extends Model<SupplierCost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Long supplierInfoId;

    @ApiModelProperty(value = "服务id(supplier_serve id)", position = 3)
    @JSONField(ordinal = 3)
    private Long serviceId;

    @ApiModelProperty(value = "费用id(cost_item id)", position = 4)
    @JSONField(ordinal = 4)
    private Long costItemId;

    @ApiModelProperty(value = "计算方式(1自动 2手动)", position = 5)
    @JSONField(ordinal = 5)
    private Integer countWay;

    @ApiModelProperty(value = "数量", position = 6)
    @JSONField(ordinal = 6)
    private Integer amount;

    @ApiModelProperty(value = "数量单位(1票 2柜 3方)", position = 7)
    @JSONField(ordinal = 7)
    private Integer amountUnit;

    @ApiModelProperty(value = "数量来源(1固定 2计费重)", position = 8)
    @JSONField(ordinal = 8)
    private Integer amountSource;

    @ApiModelProperty(value = "单价", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer cid;

    @ApiModelProperty(value = "备注", position = 11)
    @JSONField(ordinal = 11)
    private String remark;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 12)
    @JSONField(ordinal = 12)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 14)
    @JSONField(ordinal = 14)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
