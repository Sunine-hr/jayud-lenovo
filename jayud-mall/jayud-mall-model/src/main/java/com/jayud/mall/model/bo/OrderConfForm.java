package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @TableField(value = "`status`")
    @JSONField(ordinal = 5)
    private String status;

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

    /*(配载单)配置对应的报价与提单*/
    @ApiModelProperty(value = "(配载单)配置对应的报价与提单-报价", position = 9)
    @JSONField(ordinal = 9)
    private List<OceanConfDetail> offerInfoDetailList;

    @ApiModelProperty(value = "(配载单)配置对应的报价与提单-提单", position = 10)
    @JSONField(ordinal = 10)
    private List<OceanConfDetail> oceanBillDetailList;

}
