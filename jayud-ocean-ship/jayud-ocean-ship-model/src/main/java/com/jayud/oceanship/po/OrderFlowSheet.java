package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderFlowSheet对象", description="")
public class OrderFlowSheet extends Model<OrderFlowSheet> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "业务类型id_code")
    private String productClassifyId;

    @ApiModelProperty(value = "业务类型")
    private String productClassifyName;

    @ApiModelProperty(value = "流程code")
    private String status;

    @ApiModelProperty(value = "流程名")
    private String statusName;

    @ApiModelProperty(value = "是否完成(0:未完成;1:已完成)")
    private String complete;

    @ApiModelProperty(value = "是否要通过(0:未完成;1:已完成)")
    private String isPass;

    @ApiModelProperty(value = "上一流程节点code")
    private String fStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
