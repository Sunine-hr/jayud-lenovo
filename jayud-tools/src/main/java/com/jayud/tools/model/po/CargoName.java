package com.jayud.tools.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 货物名称表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CargoName对象", description="货物名称表")
public class CargoName extends Model<CargoName> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "序号")
    private Long xh;

    @ApiModelProperty(value = "袋号")
    private String dh;

    @ApiModelProperty(value = "袋重")
    private BigDecimal dz;

    @ApiModelProperty(value = "圆通单号")
    private String ytdh;

    @ApiModelProperty(value = "提单号")
    private String tdh;

    @ApiModelProperty(value = "数量(PCS)")
    private Integer sl;

    @ApiModelProperty(value = "重量(PCS)")
    private BigDecimal zl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "货品名称")
    private String hpmc;

    @ApiModelProperty(value = "件数")
    private Integer js;

    @ApiModelProperty(value = "PCE")
    private String pce;

    @ApiModelProperty(value = "价值")
    private BigDecimal jz;

    @ApiModelProperty(value = "姓名1")
    private String xm1;

    @ApiModelProperty(value = "姓名2")
    private String xm2;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "号码1")
    private String hm1;

    @ApiModelProperty(value = "姓名3")
    private String xm3;

    @ApiModelProperty(value = "号码2")
    private String hm2;

    @ApiModelProperty(value = "标记单号")
    private String bjdh;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
