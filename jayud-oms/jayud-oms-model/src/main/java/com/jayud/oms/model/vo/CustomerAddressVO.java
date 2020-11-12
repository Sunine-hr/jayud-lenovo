package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户地址
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class CustomerAddressVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地址id")
    private Long id;

    @ApiModelProperty(value = "地址类型（0 提货地址 1送货地址）")
    private String type;

    @ApiModelProperty(value = "客户地址id")
    private Long customerId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "邮编")
    private String postcode;

    @ApiModelProperty(value = "状态（0无效 1有效）")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


//    public void setType(String type) {
//        this.type = AddressTypeEnum.getDesc(type);
//    }

    /**
     * 拼装详细地址
     */
    public void splicingAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.province).append(this.city)
                .append(this.area).append(" ").append(this.address);
        this.address = sb.toString();
    }

}
