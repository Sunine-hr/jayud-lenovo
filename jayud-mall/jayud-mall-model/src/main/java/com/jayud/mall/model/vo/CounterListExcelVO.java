package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "CounterListExcelVO", description = "装柜清单Excel导出数据VO")
@Data
public class CounterListExcelVO {

    //**表头**

    @ApiModelProperty(value = "柜子清单id")
    private Long listInfoId;
    @ApiModelProperty(value = "柜子id")
    private Long counterId;
    @ApiModelProperty(value = "提单id")
    private Long billId;


    //提单号
    @ApiModelProperty(value = "提单号")
    private String billNo;
    //柜型
    @ApiModelProperty(value = "柜型")
    private String cabinetCode;
    //柜号
    @ApiModelProperty(value = "柜号")
    private String cntrNo;
    //柜子的创建时间
    @ApiModelProperty(value = "柜子的创建时间")
    private String createTime;
    //开船日期
    @ApiModelProperty(value = "开船日期")
    private String sailTime;
    //总箱数
    @ApiModelProperty(value = "总箱数")
    private String totalCartons;
    //总重量
    @ApiModelProperty(value = "总重量")
    private String totalWeight;
    //总体积
    @ApiModelProperty(value = "总体积")
    private String totalVolume;

    //**表明细**
    @ApiModelProperty(value = "明细list")
    private List<CounterOrderInfoExcelVO> counterOrderInfoExcelList;



}
