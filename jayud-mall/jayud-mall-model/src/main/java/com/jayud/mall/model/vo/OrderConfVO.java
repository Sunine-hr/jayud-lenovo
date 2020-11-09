package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "配载单信息")
public class OrderConfVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "配载单号")
    private String orderNo;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "目的国家(harbour_info id_code)")
    private String harbourCode;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "运输方式name")
    private String tName;

    @ApiModelProperty(value = "目的国家name")
    private String harbourName;

    /*报价信息list*/
    @ApiModelProperty(value = "报价信息list")
    private List<OfferInfoVO> offerInfoVOList;

    /*提单信息list*/
    @ApiModelProperty(value = "提单信息list")
    private List<OceanBillVO> oceanBillVOList;


}
