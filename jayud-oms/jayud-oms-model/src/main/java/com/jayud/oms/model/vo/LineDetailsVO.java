package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 线路详情
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
@Data
public class LineDetailsVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "更新人")
    private String upUser;

    @ApiModelProperty(value = "线路编号")
    private String lineCode;

    @ApiModelProperty(value = "线路类型(城配/干线等-数据字典配置)")
    private String lineType;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "出发地")
    private String fromAddress;

    @ApiModelProperty(value = "出发地-省")
    private Long fromCountry;

    @ApiModelProperty(value = "出发地-省")
    private Long fromProvince;

    @ApiModelProperty(value = "出发地-城市")
    private Long fromCity;

    @ApiModelProperty(value = "出发地-区县")
    private Long fromRegion;

    @ApiModelProperty(value = "目的地")
    private String toAddress;

    @ApiModelProperty(value = "目的地-省")
    private Long toProvince;

    @ApiModelProperty(value = "目的地-城市")
    private Long toCity;

    @ApiModelProperty(value = "目的地-区县")
    private Long toRegion;

    @ApiModelProperty(value = "路由属性(自营/加盟-数据字典配置)")
    private String routeAttribute;

    @ApiModelProperty(value = "里程(km)")
    private Double mileage;

    @ApiModelProperty(value = "时效(h)")
    private Double prescription;

    @ApiModelProperty(value = "日均里程(km)")
    private Double averageMileage;

    @ApiModelProperty(value = "审核状态(1-待审核 2-审核通过 3-终止 0-拒绝)")
    private Integer auditStatus;

    @ApiModelProperty(value = "途经点")
    private String passing;

    @ApiModelProperty(value = "路线路由")
    private String lineRoute;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
