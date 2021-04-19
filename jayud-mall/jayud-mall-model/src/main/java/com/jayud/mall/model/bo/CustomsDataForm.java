package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CustomsDataForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "IDcode，报关ID", required = true, position = 2)
    @JSONField(ordinal = 2)
    @NotBlank(message = "报关ID,不能为空")
    private String idCode;

    @ApiModelProperty(value = "报关国家代码(country code)", position = 3)
    @JSONField(ordinal = 3)
    private String cnCode;

    @ApiModelProperty(value = "报关HSCODE", position = 4)
    @JSONField(ordinal = 4)
    private String hsCode;

    @ApiModelProperty(value = "报关中文品名", position = 5)
    @JSONField(ordinal = 5)
    private String chName;

    @ApiModelProperty(value = "报关英文品名", position = 6)
    @JSONField(ordinal = 6)
    private String enName;

    @ApiModelProperty(value = "材质", position = 7)
    @JSONField(ordinal = 7)
    private String texture;

    @ApiModelProperty(value = "用途", position = 8)
    @JSONField(ordinal = 8)
    private String uses;

    @ApiModelProperty(value = "申报单价(申报价格)", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal declarePrice;

    @ApiModelProperty(value = "申报单价单位(币种)(currency_info id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer cid;

    @ApiModelProperty(value = "净重", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal suttle;

    @ApiModelProperty(value = "净重单位", position = 12)
    @JSONField(ordinal = 12)
    private String suttleUnit;

    @ApiModelProperty(value = "毛重", position = 13)
    @JSONField(ordinal = 13)
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "毛重单位", position = 14)
    @JSONField(ordinal = 14)
    private String grossUnit;

    @ApiModelProperty(value = "退税率%", position = 15)
    @JSONField(ordinal = 15)
    private BigDecimal drawbackTax;

    @ApiModelProperty(value = "关税率%", position = 16)
    @JSONField(ordinal = 16)
    private BigDecimal efundRates;

    @ApiModelProperty(value = "附件费", position = 17)
    @JSONField(ordinal = 17)
    private BigDecimal annexFee;

    @ApiModelProperty(value = "图片", position = 18)
    @JSONField(ordinal = 18)
    private String picUrl;

    @ApiModelProperty(value = "申报要素", position = 19)
    @JSONField(ordinal = 19)
    private String declareElements;

}
