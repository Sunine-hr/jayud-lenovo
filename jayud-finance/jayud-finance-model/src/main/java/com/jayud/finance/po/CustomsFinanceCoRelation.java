package com.jayud.finance.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@ApiModel(value="CustomsFinanceCoRelation对象", description="云报关-财务金蝶中的供应商/客户公司名称对应关系表")
public class CustomsFinanceCoRelation extends Model<CustomsFinanceCoRelation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String yunbaoguanName;

    private String kingdeeName;

    private String kingdeeCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
