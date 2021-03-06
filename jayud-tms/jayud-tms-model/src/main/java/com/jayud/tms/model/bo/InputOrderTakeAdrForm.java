package com.jayud.tms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 提货/收货地址
 */
@Data
public class InputOrderTakeAdrForm {

    @ApiModelProperty(value = "提货/送货地址")
    private String address;

    @ApiModelProperty(value = "所属客户ID")
    private Long customerId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    //以上部分是加自定义地址使用
    //以下部分是选择地址使用,通过deliveryId区分

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Long deliveryId;

    @ApiModelProperty(value = "提货/送货信息ID")
    private Long takeAdrId;

    @ApiModelProperty(value = "提货日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime takeTimeStr;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "件数")
    private Integer pieceAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer oprType;

    @ApiModelProperty(value = "入仓号")
    private String enterWarehouseNo;

    @ApiModelProperty(value = "省主键")
    private Long province;

    @ApiModelProperty(value = "市主键")
    private Long city;

    @ApiModelProperty(value = "区主键")
    private Long area;

    @ApiModelProperty(value = "提货文件上传附件地址数组集合")
    private List<FileView> takeFiles = new ArrayList<>();

    @ApiModelProperty(value = "是否香港配送")
    private Boolean isHkDelivery = false;

}
