package com.jayud.oms.model.po;

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
 * 订单完成情况表
 * </p>
 *
 * @author LDR
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderFlowSheet对象", description="订单完成情况表")
public class OrderFlowSheet extends Model<OrderFlowSheet> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "业务类型id")
    private String productClassifyId;

    @ApiModelProperty(value = "业务类型")
    private String productClassifyName;

    @ApiModelProperty(value = "流程code")
    private String status;

    @ApiModelProperty(value = "流程名")
    private String statusName;

    @ApiModelProperty(value = "是否完成(0:未完成;1:已完成)")
    private String complete;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "上一流程节点code")
    private String fStatus;

    @ApiModelProperty(value = "是否要通过(0:未完成;1:已完成)")
    private String isPass;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
