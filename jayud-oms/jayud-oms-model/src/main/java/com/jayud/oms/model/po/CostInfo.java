package com.jayud.oms.model.po;

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
 * 费用名描述
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CostInfo对象", description="费用名描述")
public class CostInfo extends Model<CostInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "费用code")
    private String idCode;

    @ApiModelProperty(value = "费用名")
    private String name;

    @ApiModelProperty(value = "状态(1应收 2应付)")
    private Integer types;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
