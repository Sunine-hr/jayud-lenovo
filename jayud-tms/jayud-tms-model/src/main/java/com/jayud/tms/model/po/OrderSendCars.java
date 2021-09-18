package com.jayud.tms.model.po;

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
 * 订单派车信息
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderSendCars对象", description="订单派车信息")
public class OrderSendCars extends Model<OrderSendCars> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运输订单号(order_transport orderno)")
    private String transportNo;

    @ApiModelProperty(value = "运输对应子订单(订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T)")
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称")
    private String cntrPicName;

    @ApiModelProperty(value = "车辆ID")
    private Long vehicleId;

    @ApiModelProperty(value = "大陆司机名ID")
    private Long driverInfoId;

    @ApiModelProperty(value = "要求")
    private String remarks;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态(0-草稿 1-审核中 2-审核驳回 3-审核通过)")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "骑师id")
    private Long jockeyId;

    @ApiModelProperty(value = "骑师姓名")
    private String jockey;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
