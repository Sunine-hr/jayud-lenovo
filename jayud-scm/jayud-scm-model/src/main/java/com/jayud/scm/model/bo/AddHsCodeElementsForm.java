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
 * 海关税号要素明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddHsCodeElementsForm {

    @ApiModelProperty(value = "要素名称")
    private String elementsName;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "排序")
    private Integer sortIndex;

    @ApiModelProperty(value = "备注")
    private String remark;

}
