package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.po.OceanConfDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "配载单")
public class OrderConfForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "配载单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer tid;

    @ApiModelProperty(value = "目的国家代码(country code)", position = 4)
    @JSONField(ordinal = 5)
    private String destinationCountryCode;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 7)
    @JSONField(ordinal = 7)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 8)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 8, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "配载名称")
    private String confName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态代码\n" +
            "\n" +
            "0:准备\n" +
            "10:启用\n" +
            "20:开始配载\n" +
            "30:转运中\n" +
            "40:完成\n" +
            "-1:取消")
    private String statusCode;

    @ApiModelProperty(value = "状态名称")
    private String statusName;


    /*(配载单)配置对应的报价与提单*/
    @ApiModelProperty(value = "(配载单)配置对应的报价与提单-这里指报价", position = 9)
    @JSONField(ordinal = 9)
    private List<OceanConfDetail> offerInfoDetailList;

    @ApiModelProperty(value = "(配载单)配置对应的报价与提单-这里指提单", position = 10)
    @JSONField(ordinal = 10)
    private List<OceanConfDetail> oceanBillDetailList;

}
