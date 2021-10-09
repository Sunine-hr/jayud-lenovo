package com.jayud.oms.model.po;

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
 * 打印报表管理
 * </p>
 *
 * @author LDR
 * @since 2021-10-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PrintReportManage对象", description = "打印报表管理")
public class PrintReportManage extends Model<PrintReportManage> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "唯一标识")
    private String uniqueIden;

    @ApiModelProperty(value = "模板地址")
    private String templateUrl;

    @ApiModelProperty(value = "传入参数(多个用逗号隔开)")
    private String params;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "模块名称")
    private String modularName;

    @ApiModelProperty(value = "报表名称")
    private String reportName;

    @ApiModelProperty(value = "状态（0无效 1有效）")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "描述")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
