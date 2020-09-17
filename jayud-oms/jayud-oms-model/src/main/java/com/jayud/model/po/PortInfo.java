package com.jayud.model.po;

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
 * 口岸基础信息
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PortInfo对象", description="口岸基础信息")
public class PortInfo extends Model<PortInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "code")
    private String idCode;

    @ApiModelProperty(value = "口岸名")
    private String name;

    @ApiModelProperty(value = "口岸地址")
    private String address;

    @ApiModelProperty(value = "状态(0可用 1不可用)")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
