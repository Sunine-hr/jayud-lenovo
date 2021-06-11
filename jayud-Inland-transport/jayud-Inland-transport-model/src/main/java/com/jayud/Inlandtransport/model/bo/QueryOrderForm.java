package com.jayud.Inlandtransport.model.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 分页查询订单列表
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class QueryOrderForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核,\n" +
            "NL_3_1派车审核不通过,NL_3_2派车审核驳回,\n" +
            "NL_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)")
    private String status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;


//    @ApiModelProperty(value = "创建时间")
//    private String createTime;

    @ApiModelProperty(value = "主订单号")
    @JsonIgnore
    private List<String> mainOrderNos;

    @ApiModelProperty(value = "流程状态 前端不用管")
    @JsonIgnore
    private List<Integer> processStatusList;

    @ApiModelProperty(value = "操作指令,cmd = costAudit 费用审核")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户,前台传")
    private String loginUserName;

    @ApiModelProperty(value = "开始日期")
    private String beginCreatedTime;

    @ApiModelProperty(value = "结束日期")
    private String endCreatedTime;

    @ApiModelProperty(value = "车型", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "子订单id集合")
    private Set<Long> subOrderIds;

    @ApiModelProperty(value = "提货时间")
    private List<String> takeTimeStr;



    public void setTakeTimeStr(List<String> takeTimeStr) {
        this.takeTimeStr = takeTimeStr;
        if (takeTimeStr != null && takeTimeStr.size() > 1) {
            String endTime = takeTimeStr.get(1);
            String[] tmp = endTime.split(" ");
            takeTimeStr.set(1, tmp[0] + " 23:59:59");
        }
    }

    public void assemblyMainOrderNo(JSONArray mainOrders) {
        mainOrderNos = new ArrayList<>(mainOrders.size());
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject tmp = mainOrders.getJSONObject(i);
            mainOrderNos.add(tmp.getStr("orderNo"));
        }
    }
}
