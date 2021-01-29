package com.jayud.oceanship.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 物流轨迹跟踪表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LogisticsTrack对象", description="物流轨迹跟踪表")
public class LogisticsTrack extends Model<LogisticsTrack> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联主订单ID")
    private String mainOrderId;

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

    @ApiModelProperty(value = "附件地址文件名称")
    private String statusPicName;

    @ApiModelProperty(value = "本次节点的操作描述")
    private String description;

    @ApiModelProperty(value = "委托号")
    private String entrustNo;

    @ApiModelProperty(value = "通关时间")
    private LocalDateTime goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private LocalDateTime preGoCustomsTime;

    @ApiModelProperty(value = "过磅数(KG)")
    private Double carWeighNum;

    @ApiModelProperty(value = "操作时间,用户录入")
    private LocalDateTime operatorTime;

    @ApiModelProperty(value = "操作人,用户录入")
    private String operatorUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "业务类型(0:空运,1:纯报关,2:中港运输)参考BusinessTypeEnum")
    private Integer type;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
