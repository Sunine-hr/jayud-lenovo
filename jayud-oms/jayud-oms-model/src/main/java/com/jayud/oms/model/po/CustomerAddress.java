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
 * 客户地址
 * </p>
 *
 * @author LDR
 * @since 2021-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomerAddress对象", description = "客户地址")
public class CustomerAddress extends Model<CustomerAddress> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地址id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "地址类型（0 提货地址 1送货地址）")
    private Integer type;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "省主键")
    private Long province;

    @ApiModelProperty(value = "市主键")
    private Long city;

    @ApiModelProperty(value = "区主键")
    private Long area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "邮编")
    private String postCode;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "百度经纬度")
    private String baiduLaAndLo;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "区code")
    private String areaCode;

    @ApiModelProperty(value = "最终地址")
    private String finalAddress;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
