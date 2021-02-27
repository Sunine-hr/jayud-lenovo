package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "提单信息")
public class OceanBillVO {


    @ApiModelProperty(value = "自增加id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer tid;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "提单号(供应商提供)", position = 4)
    @JSONField(ordinal = 4)
    private String orderId;

    @ApiModelProperty(value = "起运港口(harbour_info idcode)", position = 5)
    @JSONField(ordinal = 5)
    private String startCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)", position = 6)
    @JSONField(ordinal = 6)
    private String endCode;

    @ApiModelProperty(value = "开船日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程", position = 8)
    @JSONField(ordinal = 8)
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)", position = 9)
    @JSONField(ordinal = 9)
    private Integer unit;

    @ApiModelProperty(value = "创建时间", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "任务分组id(提单任务task_group id)", position = 11)
    @JSONField(ordinal = 11)
    private Integer taskId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 12)
    @JSONField(ordinal = 12)
    private Integer operationTeamId;

    /*关联信息*/
    @ApiModelProperty(value = "运输方式名称", position = 13)
    private String tname;

    @ApiModelProperty(value = "供应商名称", position = 14)
    private String supplierName;

    @ApiModelProperty(value = "起运港口名称", position = 15)
    private String startName;

    @ApiModelProperty(value = "目的港口名称", position = 16)
    private String endName;

    @ApiModelProperty(value = "任务分组名称", position = 17)
    private String taskName;

    @ApiModelProperty(value = "运营(服务)小组名称", position = 18)
    private String operationTeamName;

    /*提单关联柜号信息*/
    @ApiModelProperty(value = "提单关联柜号list", position = 19)
    @JSONField(ordinal = 19)
    private List<OceanCounterVO> oceanCounterVOList;

    //提单id
    @ApiModelProperty(value = "提单id", position = 20)
    @JSONField(ordinal = 20)
    private Integer tdId;

    //起运港/目的港
    @ApiModelProperty(value = "起运港/目的港", position = 21)
    @JSONField(ordinal = 21)
    private String startEndName;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 22)
    @JSONField(ordinal = 22)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 23)
    @JSONField(ordinal = 23)
    private String userName;

    //航程（带单位格式化）
    @ApiModelProperty(value = "航程（带单位格式化）", position = 24)
    @JSONField(ordinal = 24)
    private String crudingRrange;

    //提单费用信息
    @ApiModelProperty(value = "提单对应的订单以及费用信息", position = 25)
    @JSONField(ordinal = 25)
    private BillCostInfoVO billCostInfoVO;

    //提单对应的订单 以及 费用信息
    @ApiModelProperty(value = "提单对应的订单以及费用信息", position = 26)
    @JSONField(ordinal = 26)
    private List<BillOrderCostInfoVO> billOrderCostInfoVOS;



}
