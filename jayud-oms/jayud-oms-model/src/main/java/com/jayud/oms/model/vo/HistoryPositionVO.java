package com.jayud.oms.model.vo;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * GPS实时定位车辆返回对象
 */
@Data
public class HistoryPositionVO {

//    @ApiModelProperty(value = "历史轨迹")
//    private List<HistoryVO> historyVOS = new ArrayList<>();

    @ApiModelProperty(value = "历史轨迹")
    List<List<Double>> lists = new ArrayList<>();

//    JSONArray data;

    @ApiModelProperty(value = "提货地址")
    private List<OrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "卸货地址")
    private List<OrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();

}
