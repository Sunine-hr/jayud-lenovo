package com.jayud.trailer.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 拖车查询列表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QueryTrailerOrderForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "拖车订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "进出口类型",required = true)
    private Integer impAndExpType;

    @ApiModelProperty(value = "车型大小")
    private Long cabinetSize;

    @ApiModelProperty(value = "状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3派车审核,TT_4拖车提柜,TT_5拖车到仓,TT_6拖车离仓,TT_7拖车过磅,TT_8确认还柜)")
    private String status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "起运港/目的港代码")
    private String portCode;

    @ApiModelProperty(value = "创建时间")
    private String[] createTime;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "so")
    private String so;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "提单号")
    private String billOfLading;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "流程状态")
    private Integer processStatus;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "主订单号集合")
    @JsonIgnore
    private List<String> mainOrderNos;

    @ApiModelProperty(value = "流程状态")
    private List<Integer> processStatusList;

    @ApiModelProperty(value = "操作指令,cmd = costAudit 费用审核")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户,前台传")
    private String loginUserName;

    @ApiModelProperty(value = "提货时间")
    private String[] takeTimeStr;

    @ApiModelProperty(value = "子订单号")
    private Set<String> subOrderNos = new HashSet<>();

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

    public void setTakeTimeStr(String[] takeTimeStr) {
        this.takeTimeStr = new String[]{takeTimeStr[0]+" 00:00:00",takeTimeStr[1]+" 23:59:59"};
    }
}
