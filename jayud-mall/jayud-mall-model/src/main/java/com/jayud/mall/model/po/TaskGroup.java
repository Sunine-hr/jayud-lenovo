package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 提单任务分组
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TaskGroup对象", description="提单任务分组")
public class TaskGroup extends Model<TaskGroup> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "分组代码")
    private String idCode;

    @ApiModelProperty(value = "分组名")
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "1提单任务分组 2运单任务分组")
    private Integer types;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
