package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典类型
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Data
public class AddDictTypeForm extends Model<AddDictTypeForm> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "字典类型主键")
    private Integer id;

    @ApiModelProperty(value = "类型名称")
    @NotEmpty(message = "类型名称必传")
    private String name;

    @ApiModelProperty(value = "类型代码")
    @NotEmpty(message = "类型代码必传")
    private String code;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
