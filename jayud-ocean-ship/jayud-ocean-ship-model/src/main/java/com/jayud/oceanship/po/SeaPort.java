package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 船港口地址表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SeaPort对象", description="船港口地址表")
public class SeaPort extends Model<SeaPort> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "船港口代码")
    private String code;

    @ApiModelProperty(value = "船港口名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
