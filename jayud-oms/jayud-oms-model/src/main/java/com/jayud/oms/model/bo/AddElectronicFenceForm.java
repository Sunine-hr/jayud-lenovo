package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class AddElectronicFenceForm extends Model<AddElectronicFenceForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "围栏名称")
    @NotBlank(message = "围栏名称不能空")
    private String name;

    @ApiModelProperty(value = "范围")
    @NotNull(message = "范围不能空")
    private Double scope;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "省主键")
    @NotNull(message = "省不能空")
    private Long province;

    @ApiModelProperty(value = "市主键")
    @NotNull(message = "市不能空")
    private Long city;

    @ApiModelProperty(value = "区主键")
    private Long area;

    @ApiModelProperty(value = "详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String addr;

    @ApiModelProperty(value = "经度")
    @NotBlank(message = "该地址不存在")
    private String lo;

    @ApiModelProperty(value = "纬度")
    @NotBlank(message = "该地址不存在")
    private String la;

    @ApiModelProperty(value = "经纬度(高德)")
    private String loAndLa;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
