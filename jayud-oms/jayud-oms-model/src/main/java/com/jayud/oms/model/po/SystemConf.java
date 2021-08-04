package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息系统配置
 * </p>
 *
 * @author LDR
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SystemConf对象", description="消息系统配置")
public class SystemConf extends Model<SystemConf> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "系统类型(1:邮箱)")
    private Integer type;

    @ApiModelProperty(value = "配置源")
    private String confData;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
