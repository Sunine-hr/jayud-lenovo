package com.jayud.common.entity;

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
 * @author chuanmei
 * @since 2020-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LogisticsTrack对象", description="物流轨迹跟踪表")
public class LogisticsTrackVO {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "关联订单ID")
    private Long orderId;

    @ApiModelProperty(value = "状态码(order_status)")
    private String status;

    @ApiModelProperty(value = "状态名")
    private String statusName;

    @ApiModelProperty(value = "状态描述")
    private String remarks;

    @ApiModelProperty(value = "附件地址，多个用逗号分开")
    private String statusPic;

    @ApiModelProperty(value = "附件名称地址，多个用逗号分开")
    private String statusPicName;

    @ApiModelProperty(value = "本次节点的操作描述")
    private String description;

    @ApiModelProperty(value = "委托号")
    private String entrustNo;

    @ApiModelProperty(value = "通关时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime preGoCustomsTime;

    @ApiModelProperty(value = "过磅数")
    private double carWeighNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operatorTime;

    @ApiModelProperty(value = "业务类型(0:空运,1:纯报关,2:中港运输)")
    private Integer type;




}
