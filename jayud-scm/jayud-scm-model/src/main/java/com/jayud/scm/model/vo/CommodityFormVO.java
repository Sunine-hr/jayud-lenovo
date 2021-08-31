package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.scm.model.enums.SignEnum;
import com.jayud.scm.model.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表,商品返回对象
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityFormVO {

//    @ApiModelProperty(value = "自动ID")
//      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //    @ApiModelProperty(value = "状态，0未审核，1已审核，2审核不通过")
    private Integer stateFlag;

    @ApiModelProperty(value = "状态")
    private String stateFlagDesc;

    @ApiModelProperty(value = "型号")
    private String skuModel;

    @ApiModelProperty(value = "品牌")
    private String skuBrand;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "单位")
    private String skuUnit;

    @ApiModelProperty(value = "海关编码")
    private String hsCodeNo;

    @ApiModelProperty(value = "关税率")
    private BigDecimal tempInRate;

    @ApiModelProperty(value = "增值税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "退税率")
    private BigDecimal tslRate;

    @ApiModelProperty(value = "香港管制")
    private String hkControl;

    @ApiModelProperty(value = "监管条件")
    private String controlMa;

    @ApiModelProperty(value = "商检条件")
    private String controlCiq;

    @ApiModelProperty(value = "申报要素")
    private String skuElements;

    @ApiModelProperty(value = "单位净重")
    private BigDecimal unitNw;

    @ApiModelProperty(value = "参考价")
    private BigDecimal referencePrice;

    @ApiModelProperty(value = "最高价")
    private BigDecimal maxPrice;

    @ApiModelProperty(value = "最低价")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "平均价")
    private BigDecimal avgPrice;

    @ApiModelProperty(value = "料号")
    private String itemNo;

    @ApiModelProperty(value = "税收分类编码")
    private String taxCode;

    @ApiModelProperty(value = "税收分类名称")
    private String taxCodeName;

    @ApiModelProperty(value = "商品编号")
    private String skuNo;

    @ApiModelProperty(value = "报关名称")
    private String skuNameHs;

    @ApiModelProperty(value = "产地")
    private String skuOrigin;

    @ApiModelProperty(value = "商品描述")
    private String skuNotes;

    @ApiModelProperty(value = "配件")
    private String accessories;

    @ApiModelProperty(value = "香港海关报关编码")
    private String hkCodeNo;

    @ApiModelProperty(value = "香港海关报关名称")
    private String hkCodeName;

    @ApiModelProperty(value = "税务归类日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taxCodeDate;

    @ApiModelProperty(value = "归类人ID")
    private Integer classBy;

    @ApiModelProperty(value = "归类人")
    private String cassByName;

    @ApiModelProperty(value = "归类时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime classByDtm;

//    @ApiModelProperty(value = "标记，0未标记，1已标记（不能报关）")
    private Integer isMark;

    @ApiModelProperty(value = "标记")
    private String isMarkDesc;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;


    public void setIsMark(Integer isMark) {
        this.isMark = isMark;
        this.isMarkDesc = SignEnum.getDesc(isMark);
    }

    public void setStateFlag(Integer stateFlag) {
        this.stateFlag = stateFlag;
        this.stateFlagDesc = StatusEnum.getDesc(stateFlag);
    }

}
