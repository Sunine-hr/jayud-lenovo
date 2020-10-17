package com.jayud.oms.model.po;

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
 * 审核信息记录表
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AuditInfo对象", description="审核信息记录表")
public class AuditInfo extends Model<AuditInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "被审核表的ID")
    private Long extId;

    @ApiModelProperty(value = "审核类别描述")
    private String auditTypeDesc;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
