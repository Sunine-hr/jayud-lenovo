package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.scm.model.vo.HsCodeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 添加商品表单
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddReviewCommodityForm {

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品编号")
    private String skuNo;

    @ApiModelProperty(value = "型号")
    private String skuModel;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "报关名称")
    private String skuNameHs;

    @ApiModelProperty(value = "单位")
    private String skuUnit;

    @ApiModelProperty(value = "品牌")
    private String skuBrand;

    @ApiModelProperty(value = "产地")
    private String skuOrigin;

    @ApiModelProperty(value = "商品描述")
    private String skuNotes;

    @ApiModelProperty(value = "配件")
    private String accessories;

    @ApiModelProperty(value = "海关编码")
    private String hsCodeNo;

    @ApiModelProperty(value = "香港海关报关编码")
    private String hkCodeNo;

    @ApiModelProperty(value = "香港海关报关名称")
    private String hkCodeName;

    @ApiModelProperty(value = "香港管制")
    private String hkControl;

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

    @ApiModelProperty(value = "税务归类日期")
    private String taxCodeDate;

    @ApiModelProperty(value = "申报要素")
    private String skuElements;

    @ApiModelProperty(value = "状态，0未审核，1已审核，2审核不通过")
    private Integer stateFlag;

    @ApiModelProperty(value = "归类人ID")
    private Integer classBy;

    @ApiModelProperty(value = "归类人")
    private String cassByName;

    @ApiModelProperty(value = "归类时间")
    private String classByDtm;

    @ApiModelProperty(value = "标记，0未标记，1已标记（不能报关）")
    private Integer isMark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private String crtByDtm;

    @ApiModelProperty(value = "申报要素集合")
    private List<AddCommodityEntryForm> addCommodityEntryForms;

    @ApiModelProperty(value = "审核商品集合")
    private List<AddCommodityDetailForm> addCommodityDetailForms;

    @ApiModelProperty(value = "海关编码对象")
    private HsCodeVO hsCodeVO;

}
