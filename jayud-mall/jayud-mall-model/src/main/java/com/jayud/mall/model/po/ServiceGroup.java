package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报价服务组
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ServiceGroup对象", description="报价服务组")
public class ServiceGroup extends Model<ServiceGroup> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "代码", position = 2)
    @JSONField(ordinal = 2)
    private String idCode;

    @ApiModelProperty(value = "名称", position = 3)
    @JSONField(ordinal = 3)
    private String codeName;

    @ApiModelProperty(value = "目的地", position = 4)
    @JSONField(ordinal = 4)
    private String destination;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 5)
    @JSONField(ordinal = 5)
    private Integer tid;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 6)
    @JSONField(ordinal = 6)
    private Integer cargoType;

    @ApiModelProperty(value = "描述", position = 7)
    @TableField("`describe`")
    @JSONField(ordinal = 7)
    private String describe;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 8)
    @TableField("`status`")
    @JSONField(ordinal = 8)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 9)
    @JSONField(ordinal = 9)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 10)
    @JSONField(ordinal = 10)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 11)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 11, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
