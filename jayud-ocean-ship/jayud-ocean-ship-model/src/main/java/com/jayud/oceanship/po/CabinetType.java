package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 柜型表
 * </p>
 *
 * @author LLJ
 * @since 2021-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CabinetType对象", description="柜型表")
public class CabinetType extends Model<CabinetType> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private Long id;

    @ApiModelProperty(value = "柜型")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "柜型大小id")
    private String cabinetSizeId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
