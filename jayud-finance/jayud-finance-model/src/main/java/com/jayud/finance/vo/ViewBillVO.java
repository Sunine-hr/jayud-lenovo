package com.jayud.finance.vo;

import com.jayud.common.utils.DateUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账单预览
 */
@Data
public class ViewBillVO {

    @ApiModelProperty(value = "接单法人(第一行,FR,公司名称)")
    private String legalName;

    @ApiModelProperty(value = "核算期")
    private String accountTermStr;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "制单人")
    private String makeUser;

    @ApiModelProperty(value = "制单时间")
    private String makeTimeStr;

    @ApiModelProperty(value = "制单人")
    private String auditUser;

    @ApiModelProperty(value = "制单时间")
    private String auditTimeStr;

    @ApiModelProperty(value = "开户银行")
    private String accountBank;

    @ApiModelProperty(value = "开户账号")
    private String accountNo;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxNo;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    public String getAccountTermStr() {
        if(!StringUtil.isNullOrEmpty(this.accountTermStr)){
            int year = Integer.valueOf(this.accountTermStr.substring(0,4));
            int month = Integer.valueOf(this.accountTermStr.substring(5,7));
            return this.accountTermStr+"-01"+"至"+ DateUtils.getLastDayOfMonth(year,month);
        }
        return "";
    }
}
