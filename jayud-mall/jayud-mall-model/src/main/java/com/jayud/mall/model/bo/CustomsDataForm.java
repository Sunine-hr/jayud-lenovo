package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.vo.TemplateUrlVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

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

    @ApiModelProperty(value = "图片文件上传")
    private TemplateUrlVO picUrls;

    @ApiModelProperty(value = "申报要素", position = 19)
    @JSONField(ordinal = 19)
    private String declareElements;

    @ApiModelProperty(value = "附加关税", position = 20)
    @JSONField(ordinal = 20)
    private BigDecimal additionalDuty;

    @ApiModelProperty(value = "消费税", position = 21)
    @JSONField(ordinal = 21)
    private BigDecimal salesTax;

    @ApiModelProperty(value = "杂税", position = 22)
    @JSONField(ordinal = 22)
    private BigDecimal sundryTax;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "规格型号")
    private String specification;

    @ApiModelProperty(value = "审核状态(0待审核 1已审核 2已取消)")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核用户id(system_user id)")
    private Integer auditUserId;

    @ApiModelProperty(value = "审核用户名(system_user name)")
    private String auditUserName;

    //服务费用
    @ApiModelProperty(value = "服务费用list")
    private List<CustomsBaseServiceCostForm> customsBaseServiceCostList;

    //申报价值
    @ApiModelProperty(value = "申报价值list")
    private List<CustomsBaseValueForm> customsBaseValueList;

    //申报文件
    @ApiModelProperty(value = "申报文件list")
    private List<CustomsBaseFileForm> customsBaseFileList;


}
