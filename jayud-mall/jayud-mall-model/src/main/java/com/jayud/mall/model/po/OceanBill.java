package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 提单表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanBill对象", description="提单表")
public class OceanBill extends Model<OceanBill> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "供应商代码(supplier_info supplier_code)")
    private String supplierCode;

    @ApiModelProperty(value = "提单号(供应商提供)")
    private String orderId;

    @ApiModelProperty(value = "起运港口(harbour_info idcode)")
    private String startCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)")
    private String endCode;

    @ApiModelProperty(value = "开船日期")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程")
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)")
    private Integer unit;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务分组id(提单任务task_group id)")
    private Integer taskId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
