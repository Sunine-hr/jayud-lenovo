package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderInfoForm extends BasePageForm{

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "通关口岸")
    private String portCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结束创建时间")
    private String endCreateTimeStr;

    @ApiModelProperty(value = "开始创建时间")
    private String beginCreateTimeStr;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "操作指令,cmd = noSubmit草稿 or submit全部 or costAudit费用审核 or outCustomsRelease外部报关放行" +
            "or goCustomsAudit通关前审核 or dataNotAll待补全")
    private String cmd;


}
