package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 增加海关编码表单
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddHsCodeForm {

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "海关税号")
    @NotNull(message = "海关编码不为空")
    private String codeNo;

    @ApiModelProperty(value = "税号名称")
    @NotNull(message = "海关编码名称不为空")
    private String codeName;

    @ApiModelProperty(value = "香港海关税号")
    @NotNull(message = "香港海关编码不为空")
    private String codeHkNo;

    @ApiModelProperty(value = "最惠国税率")
    @NotNull(message = "最惠国税率不为空")
    private BigDecimal lowRate;

    @ApiModelProperty(value = "普通国税率")
    @NotNull(message = "普通国税率不为空")
    private BigDecimal hightRate;

    @ApiModelProperty(value = "出口税率")
    @NotNull(message = "出口税率不为空")
    private BigDecimal outRate;

    @ApiModelProperty(value = "增值税率")
    @NotNull(message = "增值税率不为空")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "退税率")
    @NotNull(message = "退税率不为空")
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
    @NotNull(message = "临时进口税率不为空")
    private BigDecimal tempInRate;

    @ApiModelProperty(value = "暂定出口税率")
    @NotNull(message = "临时出口税率不为空")
    private BigDecimal tempOutRate;

    @ApiModelProperty(value = "描述")
    private String noteS;

    @ApiModelProperty(value = "申报要素")
    private String elements;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "海关申报要素")
    private List<AddHsCodeElementsForm> hsCodeElementsForms;

}
