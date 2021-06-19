package com.jayud.storage.model.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 入仓参数
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QueryStorageFastOrderForm extends BasePageForm {

    @ApiModelProperty(value = "入库订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态()")
    private String status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "创建时间")
    private String[] createTime;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "出仓号")
    private String outWarehouseNumber;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String legalName;

    @ApiModelProperty(value = "流程状态")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态")
    private List<Integer> processStatusList;

    @ApiModelProperty(value = "主订单号")
    @JsonIgnore
    private List<String> mainOrderNos;

    @ApiModelProperty(value = "子订单号")
    @JsonIgnore
    private List<String> subPaymentOrderNos;

    @ApiModelProperty(value = "子订单号")
    @JsonIgnore
    private List<String> subReceviableOrderNos;

    @ApiModelProperty(value = "操作指令,cmd = costAudit 费用审核")
    private String cmd;

    @ApiModelProperty(value = "子订单号")
    @JsonIgnore
    private List<String> orderNos;

    @ApiModelProperty(value = "附近信息")
    private String loginUserName;

    @ApiModelProperty(value = "当前登录用户,前台传")
    private List<FileView> fileList;

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
            this.startTime = time[0] + " 00:00:00";
            this.endTime = time[1] + " 23:59:59";
        }
    }
}
