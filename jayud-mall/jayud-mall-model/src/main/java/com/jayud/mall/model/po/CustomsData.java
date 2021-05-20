package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 报关资料表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomsData对象", description="报关资料表")
public class CustomsData extends Model<CustomsData> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "IDcode，报关ID", position = 2)
    @JSONField(ordinal = 2)
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

    @ApiModelProperty(value = "申报单价", position = 9)
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

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 20)
    @JSONField(ordinal = 20)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 21)
    @JSONField(ordinal = 21)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 22)
    @JSONField(ordinal = 22)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 23)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 23)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "附加关税", position = 24)
    @JSONField(ordinal = 24)
    private BigDecimal additionalDuty;

    @ApiModelProperty(value = "消费税", position = 25)
    @JSONField(ordinal = 25)
    private BigDecimal salesTax;

    @ApiModelProperty(value = "杂税", position = 26)
    @JSONField(ordinal = 26)
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
