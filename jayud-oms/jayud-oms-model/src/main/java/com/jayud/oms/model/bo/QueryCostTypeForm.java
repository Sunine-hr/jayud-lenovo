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

/**
 * <p>
 * 费用类型，暂时废弃
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Data
public class QueryCostTypeForm extends BasePageForm{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "费用类别代码")
    private String code;

    @ApiModelProperty(value = "费用类别")
    private String codeName;

}
