package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
public class ShelvesForm extends Model<ShelvesForm> {

    @ApiModelProperty(value = "货架名称")
    @NotNull(message = "仓库id不为空")
    @Length(max = 2, message = "货架名称不能超过2个字符")
    @Pattern(regexp = "^[A-Za-z0-9]{2}$", message = "货架名称限制：最多2字符，包含文字、字母和数字")
    private String name;

}
