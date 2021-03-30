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
 * 提单工单表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WorkBill对象", description="提单工单表")
public class WorkBill extends Model<WorkBill> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id(工单id)", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "工单编号", position = 2)
    @JSONField(ordinal = 2)
    private String workNo;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 3)
    @JSONField(ordinal = 3)
    private Long billId;

    @ApiModelProperty(value = "提单号(供应商提供)(冗余字段，减少查询)(ocean_bill order_id)", position = 4)
    @JSONField(ordinal = 4)
    private String billNo;

    @ApiModelProperty(value = "优先级(1重要 2普通)", position = 5)
    @JSONField(ordinal = 5)
    private Integer priority;

    @ApiModelProperty(value = "问题描述", position = 6)
    @JSONField(ordinal = 6)
    private String problemDescription;

    @ApiModelProperty(value = "文件附件", position = 7)
    @JSONField(ordinal = 7)
    private String fileUrl;

    @ApiModelProperty(value = "状态(1进行中 2已结单 3待评价 4已关闭)", position = 8)
    @JSONField(ordinal = 8)
    private Integer status;

    @ApiModelProperty(value = "工单类型(1普通工单)", position = 9)
    @JSONField(ordinal = 9)
    private Integer type;

    @ApiModelProperty(value = "创建人(后台用户id)(system_user id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer createId;

    @ApiModelProperty(value = "提交时间", position = 11)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 11, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submissionTime;

    @ApiModelProperty(value = "操作人(后台用户、用户id)(system_user id)", position = 12)
    @JSONField(ordinal = 12)
    private Long operator;

    @ApiModelProperty(value = "操作时间", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 13, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "问题回复(操作人回复)", position = 14)
    @JSONField(ordinal = 14)
    private String revert;

    @ApiModelProperty(value = "评价", position = 15)
    @JSONField(ordinal = 15)
    private String evaluation;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
