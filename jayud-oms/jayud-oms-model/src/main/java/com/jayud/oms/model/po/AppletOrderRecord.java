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
 * 小程序订单记录
 * </p>
 *
 * @author LDR
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AppletOrderRecord对象", description = "小程序订单记录")
public class AppletOrderRecord extends Model<AppletOrderRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "中港订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "口岸名称")
    private String portName;

    @ApiModelProperty(value = "状态(1:取消)")
    private Integer recordStatus;

    @ApiModelProperty(value = "省（提货）")
    private String pickUpProvince;

    @ApiModelProperty(value = "市（提货）")
    private String pickUpCity;

    @ApiModelProperty(value = "区(提货)")
    private String pickUpArea;

    @ApiModelProperty(value = "提货经纬度")
    private String pickLaAndLo;

    @ApiModelProperty(value = "省（送货）")
    private String receivingProvince;

    @ApiModelProperty(value = "市（送货）")
    private String receivingCity;

    @ApiModelProperty(value = "区(送货)")
    private String receivingArea;

    @ApiModelProperty(value = "送货经纬度")
    private String receivingLaAndLo;

    @ApiModelProperty(value = "货物信息")
    private String goodsDesc;

    @ApiModelProperty(value = "中港订单时间")
    private String time;

    @ApiModelProperty(value = "送货详细地址(中转仓库)")
    private String address;

    @ApiModelProperty(value = "联系人(中转仓库)")
    private String contacts;

    @ApiModelProperty(value = "联系电话(中转仓库)")
    private String contactNumber;

    @ApiModelProperty(value = "是否虚拟仓")
    private String isVirtual;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "订单状态")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
