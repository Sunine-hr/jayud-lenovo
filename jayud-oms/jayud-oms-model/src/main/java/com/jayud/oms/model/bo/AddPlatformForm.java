package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * <p>
 * 月台管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
@Data
public class AddPlatformForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "月台编号", required = true)
    @NotEmpty(message = "线路编号不能为空")
    @Length(max = 14, message = "线路编号最大长度14位")
    @Length(min = 14, message = "线路编号长度为14位")
    private String platformCode;

    @ApiModelProperty(value = "月台名称", required = true)
    @NotEmpty(message = "线路类型不能为空")
    private String platformName;

//    @ApiModelProperty(value = "所属仓库")
//    private Long warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "摄像头地址")
    private String cameraUrl;

    @ApiModelProperty(value = "月台状态(0-空闲中1-排版中2-使用中)")
    private Integer platformStatus;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
