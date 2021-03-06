package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * CrmCustomerFollow 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_跟进记录(crm_customer_follow)对象", description="基本档案_客户_跟进记录(crm_customer_follow)")
public class CrmCustomerFollow extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "联系人")
    private String linkmanName;

    @ApiModelProperty(value = "联系人id")
    private Long linkmanNameId;

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





}
