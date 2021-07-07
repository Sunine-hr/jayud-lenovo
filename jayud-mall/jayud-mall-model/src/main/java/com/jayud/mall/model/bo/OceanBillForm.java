package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "提单")
public class OceanBillForm {

    @ApiModelProperty("配载id(order_conf id)")
    private Long orderConfId;

    @ApiModelProperty(value = "自增加id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "运输方式必填")
    private Integer tid;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
//    @NotNull(message = "供应商必填")
    private Integer supplierId;

    @ApiModelProperty(value = "提单号(供应商提供)", position = 4)
    @JSONField(ordinal = 4)
    @NotNull(message = "提单号必填")
    private String orderId;

    @ApiModelProperty(value = "起运港口(harbour_info idcode)", position = 5)
    @JSONField(ordinal = 5)
    @NotNull(message = "起运港口必填")
    private String startCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)", position = 6)
    @JSONField(ordinal = 6)
    @NotNull(message = "目的港口必填")
    private String endCode;

    @ApiModelProperty(value = "开船日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "目的港口必填")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程", position = 8)
    @JSONField(ordinal = 8)
    @NotNull(message = "航程必填")
    private Integer voyageDay;

    @ApiModelProperty(value = "航程单位(1小时 2天 3月)", position = 9)
    @JSONField(ordinal = 9)
    @NotNull(message = "航程单位必填")
    private Integer unit;

    @ApiModelProperty(value = "创建时间", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务分组id(提单任务task_group id)", position = 11)
    @JSONField(ordinal = 11)
    @NotNull(message = "任务分组必填")
    private Integer taskId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id),多个用逗号分隔", position = 12)
    @JSONField(ordinal = 12)
    @NotNull(message = "运营小组必填")
    private List<Integer> operationTeamId;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 14)
    @JSONField(ordinal = 14)
    private String userName;

    @ApiModelProperty(value = "提单名称")
    private String billName;

    @ApiModelProperty(value = "提单备注")
    private String billRemark;

    //1提单对应1货柜，(PS:之前是1提单对应N货柜，现在还是用list，不改了，限制list的大小为1)
    @ApiModelProperty(value = "提单对应货柜信息list(PS:之前是1提单对应N货柜，现在还是用list，不改了，限制list的大小为1)", position = 15, required = true)
    @JSONField(ordinal = 15)
    //@NotEmpty(message = "提单对应货柜信息list必填") 非必填
    private List<OceanCounterForm> oceanCounterForms;


    @ApiModelProperty(value = "费用信息")
    private List<FeeCopeWithForm> feeCopeWithList;

}
