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

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "六联单号文件地址")
    private String encodeUrl;

    @ApiModelProperty(value = "六联单号文件地址名称")
    private String encodeUrlName;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private Integer vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称")
    private String cntrPicName;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierInfoId;

    @ApiModelProperty(value = "大陆车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "大陆司机名")
    private String driverName;

    @ApiModelProperty(value = "HK车牌号")
    private String hkLicensePlate;

    @ApiModelProperty(value = "HK司机名")
    private String hkDriverName;

    @ApiModelProperty(value = "仓库ID)")
    private Long warehouseInfoId;

    @ApiModelProperty(value = "要求")
    private String remarks;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态(0-草稿 1-审核中 2-审核驳回 3-审核通过)")
    private String status;

    @ApiModelProperty(value = "审核信息")
    private Long auditInfoId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
