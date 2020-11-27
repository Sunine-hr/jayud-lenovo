package com.jayud.mall.model.po;

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
 * 运单(订单）任务关联
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WaybillTaskRelevance对象", description="运单(订单）任务关联")
public class WaybillTaskRelevance extends Model<WaybillTaskRelevance> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderInfoId;

    @ApiModelProperty(value = "任务代码(waybill_task task_code)")
    private String taskCode;

    @ApiModelProperty(value = "任务名(waybill_task task_name)")
    private String taskName;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "天数标识")
    private String dayFlag;

    @ApiModelProperty(value = "执行人")
    private String operators;

    @ApiModelProperty(value = "完成这个任务所需的分钟数")
    private String minutes;

    @ApiModelProperty(value = "完成这个任务的考核得分")
    private Integer score;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "状态(0未激活 1已激活 2异常 3已完成)")
    private String status;

    @ApiModelProperty(value = "延期原因")
    private String reason;

    @ApiModelProperty(value = "延期/完成时间")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "操作人id")
    private Integer userId;

    @ApiModelProperty(value = "操作人名字")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
