package com.jayud.scm.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBookingOrderForm extends BasePageForm {

    //委托单日期


    //确认报关日期

    //订车日期

    //审核状态

    //查询条件
    //委托单号
    //客户名称
    @ApiModelProperty(value = "客户名称、利润中心")
    private String customerName;
    //报关单号

    //委托单状态

    @ApiModelProperty(value = "委托单号")
    private String bookingNo;

    @ApiModelProperty(value = "业务类型/工作单类型 1进口  2出口 3国内 4香港  5采购  6销售")
    private Integer modelType;

    @ApiModelProperty(value = "作用于新增出库单的委托订单查询")
    private Integer type;

    @ApiModelProperty(value = "港车id")
    private Integer hgTruckId;

    @ApiModelProperty(value = "用于判断是否为进项票新增")
    private Integer isExportTaxInvoice;

//    @ApiModelProperty("搜索条件")
//    private String condition;
//
//    @ApiModelProperty("搜索key")
//    private String key;
//
//    @ApiModelProperty("入库日期")
//    private String[] createTime;
//
//    @ApiModelProperty("开始时间")
//    @JsonIgnore
//    private String startTime;
//
//    @ApiModelProperty("结束时间")
//    @JsonIgnore
//    private String endTime;
//
//    public void setTime(){
//        if(createTime != null && createTime.length>0){
//            startTime = createTime[0];
//            endTime = createTime[1];
//        }
//    }

}
