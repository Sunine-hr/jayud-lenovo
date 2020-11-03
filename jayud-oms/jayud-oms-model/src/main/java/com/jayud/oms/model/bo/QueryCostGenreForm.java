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
 * 基础数据费用类型
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
@Data
public class QueryCostGenreForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "费用类型代码")
    private String code;

    @ApiModelProperty(value = "费用类型名称")
    private String name;

    @ApiModelProperty(value = "状态（1启用，0禁用）")
    private String status;

}
