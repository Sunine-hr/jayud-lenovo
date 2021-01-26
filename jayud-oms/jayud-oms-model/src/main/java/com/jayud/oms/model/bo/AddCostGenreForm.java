package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class AddCostGenreForm extends Model<AddCostGenreForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键,修改时必传")
    private Long id;

    @ApiModelProperty(value = "费用类型代码", required = true)
    @NotEmpty(message = "code is required")
    private String code;

    @ApiModelProperty(value = "费用类型名称", required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "描述")
    private String remarks;


}
