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
 * 电子围栏
 * </p>
 *
 * @author LDR
 * @since 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ElectronicFence对象", description = "电子围栏")
public class QueryElectronicFenceForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "围栏编号")
    private String number;

    @ApiModelProperty(value = "围栏名称")
    private String name;

}
