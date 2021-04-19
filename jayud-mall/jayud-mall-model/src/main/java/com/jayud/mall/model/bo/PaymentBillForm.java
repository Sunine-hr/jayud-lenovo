package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.vo.TemplateUrlVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentBillForm {

    @ApiModelProperty(value = "应付账单主单id pay_bill_master id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "应付账单主单id不能为空")
    private Long id;

    @ApiModelProperty(value = "金额", position = 5)
    @JSONField(ordinal = 5)
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    @NotNull(message = "币种不能为空")
    private Integer cid;

    @ApiModelProperty(value = "交易凭证(url)文件上传", position = 15)
    @JSONField(ordinal = 15)
    private List<TemplateUrlVO> voucherUrls;



}
