package com.jayud.oms.model.po;

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
 * 客户线路管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomerLine对象", description="客户线路管理")
public class CustomerLine extends Model<CustomerLine> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户线路名称")
    private String customerLineName;

    @ApiModelProperty(value = "客户线路编号")
    private String customerLineCode;

    @ApiModelProperty(value = "司机ID")
    private Long driverInfoId;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "线路ID")
    private Long lineId;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "线路规则(周一/周日等，多个用,拼接，-数据字典配置)")
    private String lineRule;

    @ApiModelProperty(value = "车牌号")
    private String vehicleNo;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "更新人")
    private String upUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
