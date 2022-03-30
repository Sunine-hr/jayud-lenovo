package com.jayud.auth.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 船港口地址表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SeaPort对象", description="船港口地址表")
public class SeaPort extends SysBaseEntity {

    @ApiModelProperty(value = "船港口代码")
    private String code;

    @ApiModelProperty(value = "船港口名称")
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
