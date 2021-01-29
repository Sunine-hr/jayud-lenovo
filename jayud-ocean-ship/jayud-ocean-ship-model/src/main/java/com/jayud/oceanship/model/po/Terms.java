package com.jayud.oceanship.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Terms对象", description="")
public class Terms extends Model<Terms> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private Integer id;

    @ApiModelProperty(value = "场景姓名")
    private String name;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
