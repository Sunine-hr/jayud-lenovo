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
 *
 * </p>
 *
 * @author LDR
 * @since 2021-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GpsPositioning对象", description = "")
public class GpsPositioning extends Model<GpsPositioning> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "车辆状态")
    private String vehicleStatus;

    @ApiModelProperty(value = "车辆方向")
    private String direction;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "速度")
    private String speed;

    @ApiModelProperty(value = "状态(1:实时,2:历史轨迹)")
    private Integer status;

    @ApiModelProperty(value = "gps厂商(1:云港通,2:北斗)")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "gps定位时间")
    private LocalDateTime gpsTime;

    @ApiModelProperty(value = "总里程")
    private Double totalMileage;

    @ApiModelProperty(value = "地理位置")
    private String addr;

    @ApiModelProperty(value = "行驶里程")
    private Double mile;

    @ApiModelProperty(value = "停车时长")
    private String stopLong;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
