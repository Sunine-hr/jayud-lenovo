package com.jayud.trailer.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Data
@Slf4j
public class TrailerDispatchVO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "拖车订单id")
    private Long orderId;

    @ApiModelProperty(value = "拖车订单号")
    private String trailerOrderNo;

    @ApiModelProperty(value = "派车单号")
    private String orderNo;

    @ApiModelProperty(value = "大陆车牌")
    private Long plateNumber;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumberName;

    @ApiModelProperty(value = "司机电话")
    private String phone;

    @ApiModelProperty(value = "司机姓名")
    private Long name;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "供应商名字(supplier_info name)")
    private String supplierName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "过磅重量")
    private Double weighing;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核意见")
    private String reviewComments;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "柜重")
    private Double cabinetWeight;
}
