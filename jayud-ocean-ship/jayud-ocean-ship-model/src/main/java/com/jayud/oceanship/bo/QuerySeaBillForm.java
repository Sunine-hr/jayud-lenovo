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
public class QuerySeaBillForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提单编号")
    private String billNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "进出口类型")
    private Integer impAndExpType;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "so")
    private String so;

    @ApiModelProperty(value = "截补料时间")
    private String[] cutReplenishTime;

    @ApiModelProperty(value = "开始截补料时间")
    private String startCutReplenishTime;

    @ApiModelProperty(value = "结束截补料时间")
    private String endCutReplenishTime;

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

    @ApiModelProperty(value = "提单或者拼柜提单  bill or spellBill")
    private String table;

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
        String[] time1 = this.cutReplenishTime;
        if(time1 != null && time1.length>0){
            this.startCutReplenishTime = time1[0];
            this.endCutReplenishTime = time1[1];
        }
    }
}
