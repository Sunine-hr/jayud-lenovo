package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-09-17
 */
@Data
public class AddReportsForm extends Model<AddReportsForm> {

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

}
