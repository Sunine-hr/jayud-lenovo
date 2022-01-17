package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 省市区关联表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RegionCity对象", description="省市区关联表")
public class RegionCity extends Model<RegionCity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "CODE")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父级ID")
    @TableField("parentId")
    private Long parentid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
