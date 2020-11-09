package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.po.OceanConfDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "配载单")
public class OrderConfForm {

    @ApiModelProperty(value = "自增id")
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "配载单号")
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    @JSONField(ordinal = 3)
    private Integer tid;

    @ApiModelProperty(value = "目的国家(harbour_info id_code)")
    @JSONField(ordinal = 4)
    private String harbourCode;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @JSONField(ordinal = 5)
    private String status;

    @ApiModelProperty(value = "创建用户id")
    @JSONField(ordinal = 6)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    @JSONField(ordinal = 6)
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JSONField(ordinal = 7)
    private LocalDateTime createTime;

    /*(配载单)配置对应的报价与提单*/
    @ApiModelProperty(value = "(配载单)配置对应的报价与提单")
    @JSONField(ordinal = 8)
    private List<OceanConfDetail> oceanConfDetailList;

}
