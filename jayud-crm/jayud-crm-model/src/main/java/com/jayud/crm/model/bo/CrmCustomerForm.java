package com.jayud.crm.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.crm.model.po.CrmCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author ciro
 * @date 2022/3/2 9:16
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_基本信息", description="基本档案_客户_基本信息")
public class CrmCustomerForm  extends CrmCustomer{

    @ApiModelProperty(value = "联系人id")
    private Long custRelationId;

    @ApiModelProperty(value = "联系人名称")
    private String custRelationUsername;

    @ApiModelProperty(value = "联系人电话")
    private String custRelationPhone;

    @ApiModelProperty(value = "联系人岗位")
    private String custRelationPostName;

    @ApiModelProperty(value = "业务类型集合")
    private List<String> businessTypesList;

    @ApiModelProperty(value = "业务类型名称")
    private String businessTypesNames;

    @ApiModelProperty(value = "对接人id")
    private Long managerUserId;

    @ApiModelProperty(value = "对接人名称")
    private String managerUsername;

    @ApiModelProperty(value = "对接人角色名称")
    private String managerUserRoleName;

    @ApiModelProperty(value = "对接人角色编码")
    private String managerUserRoleCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessTypesName;

    @ApiModelProperty(value = "是否用户新增")
    private Boolean isCustAdd;

    @ApiModelProperty(value = "是否修改业务类型")
    private Boolean isChangeBusniessType;


    @ApiModelProperty(value = "创建开始时间")
    private String createStartTime;

    @ApiModelProperty(value = "创建结束时间")
    private String createEndTime;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "客户状态文本")
    private String custStateText;

    @ApiModelProperty(value = "客户星级文本")
    private String custLevelText;

    @ApiModelProperty(value = "业务类型")
    private Map<String,String> businessTypesData;

    @ApiModelProperty(value = "服务类型集合")
    private List<String> serviceTypeList;

}
