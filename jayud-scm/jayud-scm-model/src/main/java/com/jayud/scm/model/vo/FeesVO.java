package com.jayud.scm.model.vo;

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
 * 费用计算公式表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeesVO {

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务类型")
    private String modelType;

    @ApiModelProperty(value = "费用名称")
    private String feeName;

    @ApiModelProperty(value = "费用类别")
    private String feeAlias;

    @ApiModelProperty(value = "计算公式")
    private String feeFormula;

    @ApiModelProperty(value = "费用标题")
    private String feeTitle;

    @ApiModelProperty(value = "备注")
    private String remark;

}
