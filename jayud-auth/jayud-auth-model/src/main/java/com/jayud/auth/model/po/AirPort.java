package com.jayud.auth.model.po;

import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

/**
 * AirPort 实体类
 *
 * @author jayud
 * @since 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="空运港口表对象", description="空运港口表")
public class AirPort extends SysBaseEntity {


    @ApiModelProperty(value = "机场code")
    private String code;

    @ApiModelProperty(value = "机场名称")
    private String name;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private Integer status;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "航线")
    private String route;

    @ApiModelProperty(value = "港口中文名")
    private String chineseName;


}
