package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Data

public class QueryDictForm extends BasePageForm {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "类型值")
    private String value;

    @ApiModelProperty(value = "代码")
    private String code;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "字典类型Code")
    private String dictTypeCode;



}
