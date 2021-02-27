package com.jayud.oms.model.po;

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
 * 订单单号记录表
 * </p>
 *
 * @author llj
 * @since 2021-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderTypeNumber对象", description="订单单号记录表")
public class OrderTypeNumber extends Model<OrderTypeNumber> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务类型(参考OrderTypeEnum)")
    private String classCode;

    @ApiModelProperty(value = "当前日期时间")
    private String date;

    @ApiModelProperty(value = "当前数值")
    private Integer number;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
