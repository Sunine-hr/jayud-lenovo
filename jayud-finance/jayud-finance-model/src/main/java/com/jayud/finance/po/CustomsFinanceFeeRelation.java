package com.jayud.finance.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 云报关-金蝶财务费用项名称，类型，税率对照表
 * </p>
 *
 * @author william.chen
 * @since 2020-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomsFinanceFeeRelation对象", description = "云报关-金蝶财务费用项名称，类型，税率对照表")
public class CustomsFinanceFeeRelation extends Model<CustomsFinanceFeeRelation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "云报关费用代码")
    @NotEmpty(message = "云报关费用代码不能为空")
    private String yunbaoguanCode;

    @ApiModelProperty(value = "云报关费用名称")
    @NotEmpty(message = "云报关名称不能为空")
    private String yunbaoguanName;

    @ApiModelProperty(value = "金蝶费用代码")
    @NotEmpty(message = "金蝶费用代码不能为空")
    private String kingdeeCode;

    @ApiModelProperty(value = "金蝶费用名称")
    @NotEmpty(message = "金蝶费用名称不能为空")
    private String kingdeeName;

    @ApiModelProperty(value = "费用类别代码")
    @NotEmpty(message = "费用类别代码不能为空")
    private String categoryCode;

    @ApiModelProperty(value = "费用类别")
    @NotEmpty(message = "费用类别不能为空")
    private String category;

    @ApiModelProperty(value = "费用类型代码")
    @NotEmpty(message = "费用类型代码不能为空")
    private String typeCode;

    @ApiModelProperty(value = "费用类型")
    @NotEmpty(message = "费用类型不能为空")
    private String type;

    @ApiModelProperty(value = "税率")
    @NotEmpty(message = "税率不能为空：1相当于1%")
    private String taxRate;

    @ApiModelProperty(value = "是否作废(0-否，1-是)")
    @NotNull(message = "不能为空,只能填数字1或0（1=是，0=否）")
    private Integer deprecated;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
