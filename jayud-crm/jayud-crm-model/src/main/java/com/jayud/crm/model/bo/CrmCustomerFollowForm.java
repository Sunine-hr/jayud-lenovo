package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.crm.model.po.CrmFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CrmCustomerFollow 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_跟进记录(crm_customer_follow)对象", description="基本档案_客户_跟进记录(crm_customer_follow)")
public class CrmCustomerFollowForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @NotBlank(message = "联系人不能为空")
    @ApiModelProperty(value = "联系人")
    private String linkmanName;

    @NotBlank(message = "联系人id不能为空")
    @ApiModelProperty(value = "联系人id")
    private Long linkmanNameId;

    @NotBlank(message = "记录类型不能为空")
    @ApiModelProperty(value = "记录类型")
    private String recordType;

    @ApiModelProperty(value = "记录内容")
    private String recordContent;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "文件上传url")
    private String uploadFileUrl;

    @ApiModelProperty(value = "联系时间")
    private LocalDateTime contactTime;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "文件上传表")
    private List<CrmFile> crmFiles;



}
