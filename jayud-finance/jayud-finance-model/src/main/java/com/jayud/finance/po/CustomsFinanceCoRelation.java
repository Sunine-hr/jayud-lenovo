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
 * 云报关-财务金蝶中的供应商/客户公司名称对应关系表
 * </p>
 *
 * @author william.chen
 * @since 2020-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomsFinanceCoRelation对象", description = "云报关-财务金蝶中的供应商/客户公司名称对应关系表")
public class CustomsFinanceCoRelation extends Model<CustomsFinanceCoRelation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "云报关公司名称")
    @NotEmpty(message = "云报关公司名称不能为空")
    private String yunbaoguanName;

    @ApiModelProperty(value = "金蝶公司名称")
    @NotEmpty(message = "金蝶公司名称不能为空")
    private String kingdeeName;

    @ApiModelProperty(value = "金蝶公司代码（客户对应应收，为CUS开头。供应商对应应付，VEN开头）")
    @NotEmpty(message = "金蝶公司代码不能为空，（客户对应应收，为CUS开头。供应商对应应付，VEN开头）")
    private String kingdeeCode;

    @ApiModelProperty(value = "是否废弃（1-是，0-否）")
    @NotNull(message = "是否作废字段不能为空，填写1或0，（1-是，0-否）")
    private Integer deprecated;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
