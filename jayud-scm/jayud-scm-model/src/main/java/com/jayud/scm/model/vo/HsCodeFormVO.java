package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 列表返回对象
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HsCodeFormVO {

//    @ApiModelProperty(value = "自动ID")
//      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "海关编码")
    private String codeNo;

    @ApiModelProperty(value = "海关编号名称")
    private String codeName;

    @ApiModelProperty(value = "香港海关编码")
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

    @ApiModelProperty(value = "单位1")
    private String unit1;

    @ApiModelProperty(value = "单位2")
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

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;


}
