package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * CrmCustomerManager 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_客户维护人(crm_customer_manager)对象", description="基本档案_客户_客户维护人(crm_customer_manager)")
public class CrmCustomerManager extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "对接用户id")
    private Long manageUserId;

    @ApiModelProperty(value = "对接用户名称")
    private String manageUsername;

    @ApiModelProperty(value = "对接用户角色编码")
    private String manageRoles;

    @ApiModelProperty(value = "对接用户角色名称")
    private String managerRolesName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "归属时间")
    private LocalDateTime generateDate;

    @ApiModelProperty(value = "业务类型编码")
    private String managerBusinessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String managerBusinessName;

    @ApiModelProperty(value = "是否负责人，0否，1是")
    private Boolean isCharger;

    @ApiModelProperty(value = "是否销售人员，0否，1是")
    private Boolean isSale;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "客户id集合")
    @TableField(exist = false)
    private List<Long> custIdList;





}
