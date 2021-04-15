package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 海关调查问卷
 * </p>
 *
 * @author LDR
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomsQuestionnaire对象", description = "海关调查问卷")
public class QueryCustomsQuestionnaireForm extends BasePageForm {


    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "记录人")
    private String recorder;

    @ApiModelProperty(value = "订单状态 (1:正常,2:提醒,3:失效)")
    private Integer orderStatus;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

}
