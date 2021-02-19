package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 柜型大小表
 * </p>
 *
 * @author LLJ
 * @since 2021-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CabinetSize对象", description="柜型大小表")
public class CabinetSize extends Model<CabinetSize> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      private Long id;

    @ApiModelProperty(value = "柜型大小名")
    private String name;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "状态")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
