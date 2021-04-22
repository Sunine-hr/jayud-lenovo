package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CustomsClearanceForm {

    @ApiModelProperty(value = "主键id，自增", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "IDcode，清关ID", position = 2)
    @JSONField(ordinal = 2)
    @NotBlank(message = "IDcode，清关ID，不能为空")
    private String idCode;

    @ApiModelProperty(value = "清关国家代码(country code)", position = 3)
    @JSONField(ordinal = 3)
    private String customsCode;

    @ApiModelProperty(value = "清关HSCODE", position = 4)
    @JSONField(ordinal = 4)
    private String hsCode;

    @ApiModelProperty(value = "清关中文品名", position = 5)
    @JSONField(ordinal = 5)
    private String chName;

    @ApiModelProperty(value = "清关英文品名", position = 6)
    @JSONField(ordinal = 6)
    private String enName;

    @ApiModelProperty(value = "材质", position = 7)
    @JSONField(ordinal = 7)
    private String texture;

    @ApiModelProperty(value = "用途", position = 8)
    @JSONField(ordinal = 8)
    private String uses;

    @ApiModelProperty(value = "申报要素", position = 9)
    @JSONField(ordinal = 9)
    private String declareElements;

    @ApiModelProperty(value = "申报单价", position = 10)
    @JSONField(ordinal = 10)
    private BigDecimal declarePrice;

    @ApiModelProperty(value = "建议客户申报单价", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal suggestPrice;

    @ApiModelProperty(value = "申报单价单位(币种)(currency_info id)", position = 12)
    @JSONField(ordinal = 12)
    private Integer cid;

    @ApiModelProperty(value = "净重", position = 13)
    @JSONField(ordinal = 13)
    private BigDecimal suttle;

    @ApiModelProperty(value = "净重单位", position = 14)
    @JSONField(ordinal = 14)
    private String suttleUnit;

    @ApiModelProperty(value = "毛重", position = 15)
    @JSONField(ordinal = 15)
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "毛重单位", position = 16)
    @JSONField(ordinal = 16)
    private String grossUnit;

    @ApiModelProperty(value = "关税率%", position = 17)
    @JSONField(ordinal = 17)
    private BigDecimal tariffTax;

    @ApiModelProperty(value = "加税HSCODE", position = 18)
    @JSONField(ordinal = 18)
    private String raiseHscode;

    @ApiModelProperty(value = "加税率%", position = 19)
    @JSONField(ordinal = 19)
    private BigDecimal raiseTax;

    @ApiModelProperty(value = "重量关税", position = 20)
    @JSONField(ordinal = 20)
    private BigDecimal weightTax;

    @ApiModelProperty(value = "重量关税单位", position = 21)
    @JSONField(ordinal = 21)
    private String weightTariffUnit;

    @ApiModelProperty(value = "重量单位", position = 22)
    @JSONField(ordinal = 22)
    private String weightUnit;

    @ApiModelProperty(value = "图片", position = 23)
    @JSONField(ordinal = 23)
    private String picUrl;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 24)
    @JSONField(ordinal = 24)
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "附加关税", position = 28)
    @JSONField(ordinal = 28)
    private BigDecimal additionalDuty;

    @ApiModelProperty(value = "消费税", position = 29)
    @JSONField(ordinal = 29)
    private BigDecimal salesTax;

    @ApiModelProperty(value = "杂税", position = 30)
    @JSONField(ordinal = 30)
    private BigDecimal sundryTax;

}
