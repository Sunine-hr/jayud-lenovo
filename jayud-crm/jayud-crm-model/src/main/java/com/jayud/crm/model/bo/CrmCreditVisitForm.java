package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * CrmCreditVisit 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_客户走访记录对象", description="基本档案_客户_客户走访记录")
public class CrmCreditVisitForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "走访人、公司")
    private String visitName;

    @ApiModelProperty(value = "客户对接人")
    private String custRelation;

    @NotBlank(message = "走访日期不能为空")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "走访日期")
    private LocalDateTime visitDate;

    @NotBlank(message = "走访结束日期不能为空")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "走访结束日期")
    private LocalDateTime endDate;

    @NotBlank(message = "走访地址不能为空")
    @ApiModelProperty(value = "走访地址")
    private String vistAddress;

    @ApiModelProperty(value = "走访事项")
    private String vistItem;

    @NotBlank(message = "客户需求不能为空")
    @ApiModelProperty(value = "客户需求")
    private String custReq;

    @NotBlank(message = "解决方案不能为空")
    @ApiModelProperty(value = "解决方案")
    private String custAnswer;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "拜访人员集合s")
    private List<Long> visitNameList;

    @ApiModelProperty(value = "创建时间")
    private List<String> creationTime;

}
