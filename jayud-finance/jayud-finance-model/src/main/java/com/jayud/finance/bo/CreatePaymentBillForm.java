package com.jayud.finance.bo;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.vo.InitComboxStrVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 暂存/提交对账单
 */
@Data
public class CreatePaymentBillForm {

    @ApiModelProperty(value = "应付出账单界面部分,生成账单时必传")
    private OrderPaymentBillForm paymentBillForm;

    //    @Valid
    @ApiModelProperty(value = "应付出账单详情界面部分", required = true)
    private List<OrderPaymentBillDetailForm> paymentBillDetailForms;

    @ApiModelProperty(value = "账单编号,生成账单时必传")
    private String billNo;

    @ApiModelProperty(value = "核算期,生成账单时必传")
    private String accountTermStr;

    @ApiModelProperty(value = "结算币种,生成账单时必传")
    private String settlementCurrency;

    @ApiModelProperty(value = "账单类别,生成账单时必传,只允许填写main or zgys or bg or ky or nl")
    private String subType;

    @ApiModelProperty(value = "操作指令 cmd=pre_create主订单出账暂存 or create主订单生成账单", required = true)
    @Pattern(regexp = "(pre_create|create)", message = "只允许填写pre_create or create")
    private String cmd;

    @ApiModelProperty(value = "登录用户", required = true)
    private String loginUserName;

    @ApiModelProperty(value = "是否自定义汇率", required = true)
    private Boolean isCustomExchangeRate = false;

    @ApiModelProperty(value = "自定义汇率")
    private List<InitComboxStrVO> customExchangeRate;

    @ApiModelProperty(value = "展示维度(1:费用项展示,2:订单维度)", required = true)
    @NotNull(message = "type is required")
    private Integer type = 1;

    /**
     * 校验出账单参数
     */
    public void checkCreateReceiveBill() {

        //校验自定义汇率
        if (isCustomExchangeRate) {
            if (CollectionUtils.isEmpty(customExchangeRate)) {
                throw new JayudBizException(400, "请配置自定义汇率");
            } else {
                for (InitComboxStrVO initComboxStrVO : customExchangeRate) {
                    if (StringUtils.isEmpty(initComboxStrVO.getNote())) {
                        throw new JayudBizException(400, "请配置自定义汇率");
                    }
                }
            }
        }
    }

    /**
     * 组装订单维度数据
     *
     * @param payCost
     * @param isMain
     */
    public void assemblyOrderDimensionData(Object payCost, Boolean isMain) {
        if (payCost == null) {
            return;
        }
        String key = isMain ? "mainOrderNo" : "orderNo";
        JSONArray jsonArray = new JSONArray(payCost);

        Map<String, OrderPaymentBillDetailForm> map = this.paymentBillDetailForms.stream().collect(Collectors.toMap(e -> isMain ? e.getOrderNo() : e.getSubOrderNo(), e -> e));
        List<OrderPaymentBillDetailForm> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            OrderPaymentBillDetailForm billDetailForm = map.get(jsonObject.getStr(key));
            OrderPaymentBillDetailForm tmp = ConvertUtil.convert(billDetailForm, OrderPaymentBillDetailForm.class);
            if (billDetailForm != null) {
                tmp.setCostId(jsonObject.getLong("id"));
                tmp.setLocalAmount(jsonObject.getBigDecimal("changeAmount"));
            }
            list.add(tmp);
        }
        this.paymentBillDetailForms = list;
    }
}
