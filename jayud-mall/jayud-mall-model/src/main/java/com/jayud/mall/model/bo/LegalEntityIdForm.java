package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LegalEntityIdForm {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空")
    private Long id;

}
