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
import java.time.LocalDateTime;

/**
 * <p>
 * 应收对账单表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AccountReceivable对象", description="应收对账单表")
public class AccountReceivable extends Model<AccountReceivable> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "对账单编号", position = 2)
    @JSONField(ordinal = 2)
    private String dzdNo;

    @ApiModelProperty(value = "法人id(legal_person id)", position = 3)
    @JSONField(ordinal = 3)
    private Long legalPersonId;

    @ApiModelProperty(value = "客户ID(customer id)", position = 4)
    @JSONField(ordinal = 4)
    private Integer customerId;

    @ApiModelProperty(value = "账期开始时间", position = 5)
    @JSONField(ordinal = 5)
    private LocalDateTime paymentDaysStart;

    @ApiModelProperty(value = "账期结束时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDaysEnd;

    @ApiModelProperty(value = "状态(0待核销 1核销完成)", position = 7)
    @JSONField(ordinal = 7)
    private Integer status;

    @ApiModelProperty(value = "制单时间", position = 8)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 8, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
