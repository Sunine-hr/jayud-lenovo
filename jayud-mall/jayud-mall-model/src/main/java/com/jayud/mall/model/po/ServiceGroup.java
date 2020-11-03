package com.jayud.mall.model.po;

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

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "代码")
    private String idCode;

    @ApiModelProperty(value = "名称")
    private String codeName;

    @ApiModelProperty(value = "目的国")
    private String destination;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "货物类型(1普货 2特货 3其他)")
    private Integer cargoType;

    @ApiModelProperty(value = "描述")
    private String describe;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime  createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
