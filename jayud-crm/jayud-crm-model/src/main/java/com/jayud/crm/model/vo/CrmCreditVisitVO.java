package com.jayud.crm.model.vo;

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

import java.time.LocalDateTime;
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
public class CrmCreditVisitVO extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "走访人、公司")
    private String visitName;

    @ApiModelProperty(value = "客户对接人")
    private String custRelation;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "走访日期")
    private LocalDateTime visitDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "走访结束日期")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "走访地址")
    private String vistAddress;

    @ApiModelProperty(value = "走访事项")
    private String vistItem;

    @ApiModelProperty(value = "客户需求")
    private String custReq;

    @ApiModelProperty(value = "解决方案")
    private String custAnswer;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "员工名称集合")
    private String userNames;

    @ApiModelProperty(value = "员工集合ids")
    private String userIds;

    @ApiModelProperty(value = "拜访人员集合s")
    private List<Long> visitNameList;


}
