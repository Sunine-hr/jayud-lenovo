package com.jayud.mall.model.po;

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
 * 提单任务关联
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BillTaskRelevance对象", description="提单任务关联")
public class BillTaskRelevance extends Model<BillTaskRelevance> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "提单id (ocean_bill id)")
    private Long oceanBillId;

    @ApiModelProperty(value = "任务代码(bill_task task_code)")
    private String taskCode;

    @ApiModelProperty(value = "任务名(bill_task task_name)")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "操作人id")
    private Integer userId;

    @ApiModelProperty(value = "操作人名字")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
