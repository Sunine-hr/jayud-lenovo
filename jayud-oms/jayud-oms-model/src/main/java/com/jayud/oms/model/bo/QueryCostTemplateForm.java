package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 查询费用模板
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryCostTemplateForm extends BasePageForm {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private Integer status;

    @ApiModelProperty(value = "类型(0应付 1应收)")
    private Integer type;

    @ApiModelProperty(value = "创建人")
    private String createUser;


}
