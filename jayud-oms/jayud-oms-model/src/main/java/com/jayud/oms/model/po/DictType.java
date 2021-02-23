package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典类型
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DictType对象", description="字典类型")
public class DictType extends Model<DictType> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "字典类型主键")
    private Integer id;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "类型代码")
    private String code;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
