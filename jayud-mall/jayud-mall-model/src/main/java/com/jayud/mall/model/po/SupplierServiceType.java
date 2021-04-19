package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 供应商服务类型
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SupplierServiceType对象", description="供应商服务类型")
public class SupplierServiceType extends Model<SupplierServiceType> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "类型代码", position = 2)
    @JSONField(ordinal = 2)
    private String typeCode;

    @ApiModelProperty(value = "类型名称", position = 3)
    @JSONField(ordinal = 3)
    private String typeName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 3)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 5)
    @JSONField(ordinal = 4)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 6)
    @JSONField(ordinal = 5)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6)
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
