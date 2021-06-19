package com.jayud.finance.bo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
public class QueryNotPaidBillForm extends BasePageForm {

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "供应商,应付必填")
    private String supplierChName;

    @ApiModelProperty(value = "结算单位,应收必填")
    private String unitAccount;

    @ApiModelProperty(value = "法人主体", required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "创建开始日期")
    private String beginCreatedTimeStr;

    @ApiModelProperty(value = "创建结束日期")
    private String endCreatedTimeStr;

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 or bg子订单操作", required = true)
    @Pattern(regexp = "(main|zgys|bg|ky|hy|nl|tc|cci|cce|ccf)", message = "只允许填写main or zgys or bg ")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户名", required = true)
    @NotEmpty(message = "legalName is required")
    private String loginUserName;

    @ApiModelProperty(value = "展示维度(1:费用项展示,2:订单维度)", required = true)
    @NotNull(message = "type is required")
    private Integer type;

    @ApiModelProperty(value = "是否查询订单地址", required = true)
    @JsonIgnore
    private Boolean isQueryOrderAddress;

    @ApiModelProperty(value = "操作时间")
    private List<String> operationTime;

    @ApiModelProperty(value = "用户名")
    private String userName;
}
