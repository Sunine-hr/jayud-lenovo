package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 月台详情
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
@Data
public class PlatformDetailsVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "月台编号")
    private String platformCode;

    @ApiModelProperty(value = "月台名称")
    private String platformName;

    @ApiModelProperty(value = "所属仓库")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "摄像头地址")
    private String cameraUrl;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "月台状态(0-空闲中1-排版中2-使用中)")
    private Integer platformStatus;

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

}
