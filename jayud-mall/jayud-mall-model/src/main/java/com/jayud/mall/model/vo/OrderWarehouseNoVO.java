package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderWarehouseNoVO {

    //进仓编号
    @ApiModelProperty(value = "进仓编号")
    private String warehouseNo;

    //开船日期
    @ApiModelProperty(value = "开船日期")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private String sailTime;

    //截仓日期
    @ApiModelProperty(value = "截仓日期")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private String jcTime;

    //箱数
    @ApiModelProperty(value = "箱数")
    private Integer totalCarton;

    //仓库地址 集货仓库
    @ApiModelProperty(value = "仓库地址")
    private String warehouseAddress;

    //(仓库)联系人 集货仓库
    @ApiModelProperty(value = "(仓库)联系人")
    private String contacts;

    //唛头list
    @ApiModelProperty(value = "唛头list")
    private List<MarkVO> markList;

}
