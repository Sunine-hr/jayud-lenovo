package com.jayud.mall.model.po;

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

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "服务代码")
    private String serveCode;

    @ApiModelProperty(value = "费用代码")
    private String costCode;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    private Integer countWay;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "数量单位(1票 2柜 3方)")
    private Integer amountUnit;

    @ApiModelProperty(value = "数量来源(1固定 2计费重)")
    private Integer amountSource;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "单价单位(1元 2角 3分)")
    private Integer unit;

    @ApiModelProperty(value = "币种(currency表id)")
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
