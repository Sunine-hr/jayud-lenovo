package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "查询报价参数")
public class QueryOfferInfoForm extends BasePageForm{

    @ApiModelProperty(value = "报价名")
    private String names;

    @ApiModelProperty(value = "开船日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "国家代码")
    private String countryCode;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    //目的仓库
    @ApiModelProperty(value = "目的仓库id(fab_warehouse id)")
    private Integer arriveWarehouseId;

    @ApiModelProperty(value = "报价单号")
    private String offerNo;


}
