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
 * 物流轨迹跟踪表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LogisticsTrack对象", description="物流轨迹跟踪表")
public class LogisticsTrack extends Model<LogisticsTrack> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "关联订单ID(order_info id)", position = 2)
    @JSONField(ordinal = 2)
    private String orderId;

    @ApiModelProperty(value = "状态码", position = 3)
    @JSONField(ordinal = 3)
    private Integer status;

    @ApiModelProperty(value = "状态描述", position = 4)
    @JSONField(ordinal = 4)
    private String statusName;

    @ApiModelProperty(value = "本次节点的操作描述", position = 5)
    @JSONField(ordinal = 5)
    private String description;

    @ApiModelProperty(value = "当前节点信息发生时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人id(system_user id)", position = 7)
    @JSONField(ordinal = 7)
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称(system_user name)", position = 8)
    @JSONField(ordinal = 8)
    private String operatorName;

    @ApiModelProperty(value = "备注")
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
