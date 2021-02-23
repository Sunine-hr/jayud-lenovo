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
 * 字典
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Data
public class AddDictForm extends Model<AddDictForm> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "字典类型主键")
    private Integer id;

    @ApiModelProperty(value = "类型值")
    @NotEmpty(message = "类型值必传")
    private String value;

    @ApiModelProperty(value = "代码")
    @NotEmpty(message = "代码必传")
    private String code;

    @ApiModelProperty(value = "字典类型code")
    @NotEmpty(message = "字典类型code必传")
    private String dictTypeCode;

    @ApiModelProperty(value = "备注")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
