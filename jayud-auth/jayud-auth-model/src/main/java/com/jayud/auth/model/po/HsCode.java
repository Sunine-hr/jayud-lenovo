package com.jayud.auth.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 海关编码表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="HsCode对象", description="海关编码表")
public class HsCode extends Model<HsCode> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "海关税号")
    private String codeNo;

    @ApiModelProperty(value = "税号名称")
    private String codeName;

    @ApiModelProperty(value = "香港海关税号")
    private String codeHkNo;

    @ApiModelProperty(value = "最惠国税率")
    private BigDecimal lowRate;

    @ApiModelProperty(value = "普通国税率")
    private BigDecimal hightRate;

    @ApiModelProperty(value = "出口税率")
    private BigDecimal outRate;

    @ApiModelProperty(value = "增值税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "退税率")
    private BigDecimal tslRate;

    @ApiModelProperty(value = "消费税率")
    private BigDecimal consumptionRate;

    @ApiModelProperty(value = "单位1代码")
    private String unit1;

    @ApiModelProperty(value = "单位2代码")
    private String unit2;

    @ApiModelProperty(value = "监管条件")
    private String controlMa;

    @ApiModelProperty(value = "商检条件")
    private String controlCiq;

    @ApiModelProperty(value = "商检监定")
    private String controlCheck;

    @ApiModelProperty(value = "暂定进口税率")
    private BigDecimal tempInRate;

    @ApiModelProperty(value = "暂定出口税率")
    private BigDecimal tempOutRate;

    @ApiModelProperty(value = "描述")
    private String noteS;

    @ApiModelProperty(value = "申报要素")
    private String elements;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
