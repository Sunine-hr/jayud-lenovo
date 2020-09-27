package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 费用类型
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CostType对象", description="")
public class CostType extends Model<CostType> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "费用类型CODE")
    private String code;

    @ApiModelProperty(value = "费用类型名称")
    private String codeName;

    @ApiModelProperty(value = "1启用 2无效")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
