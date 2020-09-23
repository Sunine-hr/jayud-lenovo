package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @author chuanmei
 * @since 2020-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LogisticsTrack对象", description="物流轨迹跟踪表")
public class LogisticsTrack extends Model<LogisticsTrack> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联主订单ID")
    private String mainOrderNo;

    @ApiModelProperty(value = "关联订单ID")
    private String orderId;

    @ApiModelProperty(value = "状态码(order_status)")
    private String status;

    @ApiModelProperty(value = "状态名")
    private String statusName;

    @ApiModelProperty(value = "状态描述")
    private String remarks;

    @ApiModelProperty(value = "附件地址，多个用逗号分开")
    private String statusPic;

    @ApiModelProperty(value = "本次节点的操作描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operatorTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
