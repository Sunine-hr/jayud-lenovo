package com.jayud.tms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 提货/收货地址详细信息
 */
@Data
public class OrderTakeAdrInfoVO {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Long deliveryId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "提货/送货信息ID")
    private Long takeAdrId;

    @ApiModelProperty(value = "提货/送货日期")
    private String takeTimeStr;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "件数")
    private Integer pieceAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer oprType;

    @ApiModelProperty(value = "入仓号,送货地址特有")
    private String enterWarehouseNo;

    @ApiModelProperty(value = "提货/送货文件")
    private String file;

    @ApiModelProperty(value = "提货/送货文件名称")
    private String fileName;

    @ApiModelProperty(value = "提货/送货文件上传附件地址数组集合")
    private List<FileView> takeFiles = new ArrayList<>();


    public void sortData(){

    }
}
