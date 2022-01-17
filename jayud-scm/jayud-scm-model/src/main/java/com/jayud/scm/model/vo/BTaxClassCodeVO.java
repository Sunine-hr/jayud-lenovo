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
 * 税务分类表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BTaxClassCodeVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类代码")
    private String taxCode;

    @ApiModelProperty(value = "分类名称")
    private String taxItemName;

    @ApiModelProperty(value = "类型名称")
    private String taxClassName;

    @ApiModelProperty(value = "备注")
    private String remark;


}
