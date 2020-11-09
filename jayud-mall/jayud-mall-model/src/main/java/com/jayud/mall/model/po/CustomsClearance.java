package com.jayud.mall.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 清关资料表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomsClearance对象", description="清关资料表")
public class CustomsClearance extends Model<CustomsClearance> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IDcode，清关ID")
    private String idCode;

    @ApiModelProperty(value = "清关国家代码")
    private String customsCode;

    @ApiModelProperty(value = "清关HSCODE")
    private String hsCode;

    @ApiModelProperty(value = "清关中文品名")
    private String chName;

    @ApiModelProperty(value = "清关英文品名")
    private String enName;

    @ApiModelProperty(value = "材质")
    private String texture;

    @ApiModelProperty(value = "用途")
    private String uses;

    @ApiModelProperty(value = "申报单价")
    private BigDecimal declarePrice;

    @ApiModelProperty(value = "建议客户申报单价")
    private BigDecimal suggestPrice;

    @ApiModelProperty(value = "申报单价单位(currency 对应id)")
    private Integer cid;

    @ApiModelProperty(value = "净重")
    private Double suttle;

    @ApiModelProperty(value = "净重单位")
    private String suttleUnit;

    @ApiModelProperty(value = "毛重")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "毛重单位")
    private String grossUnit;

    @ApiModelProperty(value = "关税税率%")
    private BigDecimal tariffTax;

    @ApiModelProperty(value = "加税HSCODE")
    private String raiseHscode;

    @ApiModelProperty(value = "加税税率%")
    private Double raiseTax;

    @ApiModelProperty(value = "重量关税")
    private Double weightTax;

    @ApiModelProperty(value = "重量关税单位")
    private String weightTariffUnit;

    @ApiModelProperty(value = "重量单位")
    private String weightUnit;

    @ApiModelProperty(value = "图片")
    private String picUrl;

    @ApiModelProperty(value = "状态(0可用 1不可用)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
