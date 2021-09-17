package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Reports对象", description="")
public class Reports extends Model<Reports> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "模块ID")
    private Integer modId;

    @ApiModelProperty(value = "模块名称")
    private String modName;

    @ApiModelProperty(value = "报表显示名称")
    private String rptName;

    @ApiModelProperty(value = "报表数径")
    private String rptPath;

    @ApiModelProperty(value = "报表文件名")
    private String rptFileName;

    @ApiModelProperty(value = "数据源参数")
    private BigDecimal paraNum;

    @ApiModelProperty(value = "数据源名称")
    private String procName;

    @ApiModelProperty(value = "是否纵向或者横向，true为纵，false为横向")
    private String directionFlag;

    @ApiModelProperty(value = "是否有子报表,true：有，false没有")
    private String isChildFlag;

    @ApiModelProperty(value = "表报参数名，多个中间用英文逗号隔开")
    private String paraStr;

    @ApiModelProperty(value = "菜单名ID")
    private Integer menuId;

    @ApiModelProperty(value = "菜单名代码")
    private String menuCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
