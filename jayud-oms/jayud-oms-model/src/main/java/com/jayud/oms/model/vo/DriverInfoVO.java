package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商对应司机信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class DriverInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "司机姓名")
    private String name;

    @ApiModelProperty(value = "是否主司机(0否 1是)")
    private String isMain;

    @ApiModelProperty(value = "香港电话")
    private String hkPhone;

    @ApiModelProperty(value = "大陆电话")
    private String phone;

    @ApiModelProperty(value = "车牌号id")
    private Long vehicleId;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "驾驶证")
    private String drivingNo;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名字")
    private String supplierName;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "密码（微信登录使用）")
    private String password;


    /**
     * 暂存车牌容器
     */
    @JsonIgnore
    List<String> list = new ArrayList<>(2);

    /**
     * 暂存车牌号码
     */
    public void setPlateNumber(String plateNumber) {
        list.add(plateNumber);
    }

    public void setHkNumber(String hkNumber) {
        list.add(hkNumber == null ? "" : hkNumber);
    }

    /**
     * 拼接车牌号
     */
    public void splicingPlateNumber() {
        StringBuilder sb = new StringBuilder();
        this.carNumber = sb.append(list.get(0)).append("/").append(list.get(1)).toString();
    }
}
