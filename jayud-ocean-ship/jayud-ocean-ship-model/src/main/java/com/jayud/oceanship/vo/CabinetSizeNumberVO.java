package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 柜型数量表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CabinetSizeNumber对象", description="柜型数量表")
public class CabinetSizeNumberVO extends Model<CabinetSizeNumberVO> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "海运订单id")
    private Long seaOrderId;

    @ApiModelProperty(value = "海运订单编号")
    private String seaOrderNo;

    @ApiModelProperty(value = "柜型")
    private String cabinetTypeSize;

    @ApiModelProperty(value = "柜量")
    private Integer number;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
