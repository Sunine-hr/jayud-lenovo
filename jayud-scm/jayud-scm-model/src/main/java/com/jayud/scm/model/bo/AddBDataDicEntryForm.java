package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 数据字典明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddBDataDicEntryForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "字典主表ID")
    private String dicId;

    @ApiModelProperty(value = "字典编码")
    private String dicCode;

    @ApiModelProperty(value = "类型描述")
    private String dicDesc;

    @ApiModelProperty(value = "是否可编辑，0可以编辑，1不能编辑")
    private Integer dicType;

    @ApiModelProperty(value = "字典取值")
    private String dataValue;

    @ApiModelProperty(value = "字典文本")
    private String dataText;

    @ApiModelProperty(value = "优先级，排序，从小到大")
    private Integer dataOrder;

    @ApiModelProperty(value = "保留字段1")
    private String reserved1;

    @ApiModelProperty(value = "保留字段2")
    private String reserved2;

    @ApiModelProperty(value = "保留字段3")
    private String reserved3;

    @ApiModelProperty(value = "保留字段4")
    private String reserved4;

    @ApiModelProperty(value = "保留字段5")
    private String reserved5;

    @ApiModelProperty(value = "备注")
    private String remark;

}
