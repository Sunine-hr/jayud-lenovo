package com.jayud.oceanship.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海运订单列表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QuerySeaOrderForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "海运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "状态(k_0待接单,k_1空运接单,k_2订舱,k_3订单入仓, k_4确认提单,k_5确认离港,k_6确认到港,k_7海外代理k_8确认签收)")
    private String status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "出发港口")
    private String portDeparture;

    @ApiModelProperty(value = "目的地港口")
    private String portDestination;

    @ApiModelProperty(value = "创建时间")
    private String[] createTime;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "主订单号")
    @JsonIgnore
    private List<String> mainOrderNos;

    @ApiModelProperty(value = "流程状态")
    private List<Integer> processStatusList;

    @ApiModelProperty(value = "操作指令,cmd = costAudit 费用审核")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户,前台传")
    private String loginUserName;

    public void assemblyMainOrderNo(JSONArray mainOrders) {
        mainOrderNos = new ArrayList<>(mainOrders.size());
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject tmp = mainOrders.getJSONObject(i);
            mainOrderNos.add(tmp.getStr("orderNo"));
        }
    }

    public void setStartTime(){
        String[] time = this.createTime;
        if(time != null && time.length>0){
            this.startTime = time[0];
            this.endTime = time[1];
        }
    }
}
