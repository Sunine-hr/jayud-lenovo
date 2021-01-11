package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务单类型信息
 * </p>
 *
 * @author
 * @since 2020-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ServiceType对象", description="服务类型信息")
public class ServiceType extends Model<ServiceType> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "type服务类型")
    private String type;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
