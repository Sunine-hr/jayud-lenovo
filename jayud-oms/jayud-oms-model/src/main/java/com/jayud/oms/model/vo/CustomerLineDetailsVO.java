package com.jayud.oms.model.vo;

import com.jayud.oms.model.po.CustomerLineRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 客户线路详情
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Data
public class CustomerLineDetailsVO {

    @ApiModelProperty(value = "自增id")
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

    @ApiModelProperty(value = "线路规则数组")
    private List<String> lineRules;

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

    @ApiModelProperty(value = "客户线路客户")
    private List<CustomerLineRelation> customerLineRelations;
}
