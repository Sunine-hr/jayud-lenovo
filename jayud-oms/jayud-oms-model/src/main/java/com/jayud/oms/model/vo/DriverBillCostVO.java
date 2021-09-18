package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 统计基础费用
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DriverBillCostVO extends Model<DriverBillCostVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "实际产生业务订单号")
    private String orderNo;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "状态(0-草稿 1-审核通过 2-审核驳回)")
    private Integer status;

    @ApiModelProperty(value = "币种code")
    private String currencyCode;

    @ApiModelProperty(value = "操作时间")
    private String operationTime;

    @ApiModelProperty(value = "文件名(逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "文件路径(逗号隔开)")
    private String files;

    @ApiModelProperty(value = "文件路径(逗号隔开)")
    private String costCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
