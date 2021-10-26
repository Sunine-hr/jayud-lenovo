package com.jayud.oms.model.vo;

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
@ApiModel(value="ElectronicFence对象", description="电子围栏")
public class ElectronicFenceVO extends Model<ElectronicFenceVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      private Long id;

    @ApiModelProperty(value = "围栏名称")
    private String name;

    @ApiModelProperty(value = "围栏编号")
    private String number;

    @ApiModelProperty(value = "范围")
    private Double scope;

    @ApiModelProperty(value = "状态（0:禁用 1:启用 2:删除）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "省主键")
    private Long province;

    @ApiModelProperty(value = "省")
    private String provinceDesc;

    @ApiModelProperty(value = "市主键")
    private Long city;

    @ApiModelProperty(value = "市")
    private String cityDesc;

    @ApiModelProperty(value = "区主键")
    private Long area;

    @ApiModelProperty(value = "区")
    private String areaDesc;

    @ApiModelProperty(value = "详细地址")
    private String addr;

    @ApiModelProperty(value = "经纬度(高德)")
    private String loAndLa;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
