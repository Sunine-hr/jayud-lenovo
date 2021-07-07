package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 提单物流轨迹跟踪表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BillLogisticsTrack对象", description = "提单物流轨迹跟踪表")
public class BillLogisticsTrack extends Model<BillLogisticsTrack> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联提单ID(ocean_bill id)")
    private String billId;

    @ApiModelProperty(value = "状态码")
    private Integer status;

    @ApiModelProperty(value = "状态描述")
    private String statusName;

    @ApiModelProperty(value = "本次节点的操作描述")
    private String description;

    @ApiModelProperty(value = "当前节点信息发生时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人id(system_user id)")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称(system_user name)")
    private String operatorName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
