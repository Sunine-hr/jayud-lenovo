package com.jayud.oms.order.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * CrmFile 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
public class CrmFileForm extends SysBaseEntity {


    @ApiModelProperty(value = "业务标识code")
    private String code;

    @ApiModelProperty(value = "业务主键")
    private Long businessId;

    @ApiModelProperty(value = "附件标识编号")
    private String crmFileNumber;

    @ApiModelProperty(value = "附件类型")
    private String fileType;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件上传url")
    private String uploadFileUrl;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;





}
