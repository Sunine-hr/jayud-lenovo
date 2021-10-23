package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月台管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Platform对象", description="月台管理")
public class Platform extends Model<Platform> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "月台状态(0-空闲中1-排版中2-使用中)")
    private Integer platformStatus;

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
