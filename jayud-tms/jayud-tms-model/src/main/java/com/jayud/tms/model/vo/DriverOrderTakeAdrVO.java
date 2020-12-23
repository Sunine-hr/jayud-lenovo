package com.jayud.tms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单对应收货地址
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data

public class DriverOrderTakeAdrVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提货/送货信息id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "提货/收货日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime takeTime;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc = "";

    @ApiModelProperty(value = "件数")
    private String pieceAmount;

    @ApiModelProperty(value = "重量")
    private String weight;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer oprType;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    public void setAddress(String address) {
        this.address = this.province + this.city
                + (StringUtils.isEmpty(this.area) ? "" : this.area)
                + address;
    }
}
