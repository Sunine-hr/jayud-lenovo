package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkOrderVO {

    @ApiModelProperty(value = "主键id(工单id)", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "工单编号", position = 2)
    @JSONField(ordinal = 2)
    private String workNo;

    @ApiModelProperty(value = "工单业务类型(1订单工单 2提单工单)", position = 3)
    @JSONField(ordinal = 3)
    private Integer businessType;

    @ApiModelProperty(value = "业务编号(订单号order_info order_no, 提单号ocean_bill order_id)", position = 4)
    @JSONField(ordinal = 4)
    private String businessNo;

    @ApiModelProperty(value = "业务id(订单id order_info id, 提单id ocean_bill id)", position = 5)
    @JSONField(ordinal = 5)
    private Long businessId;

    @ApiModelProperty(value = "优先级(1重要 2普通)", position = 6)
    @JSONField(ordinal = 6)
    private Integer priority;

    @ApiModelProperty(value = "问题描述", position = 7)
    @JSONField(ordinal = 7)
    private String problemDescription;

    @ApiModelProperty(value = "文件附件", position = 8)
    @JSONField(ordinal = 8)
    private String fileUrl;

    @ApiModelProperty(value = "状态(1进行中 2已结单 3待评价 4已关闭)", position = 9)
    @JSONField(ordinal = 9)
    private Integer status;

    @ApiModelProperty(value = "工单类型(1普通工单)", position = 10)
    @JSONField(ordinal = 10)
    private Integer workType;

    @ApiModelProperty(value = "创建人类型(1客户 2后台用户)", position = 11)
    @JSONField(ordinal = 11)
    private Integer creatorType;

    @ApiModelProperty(value = "创建人(客户id customer id, 用户id system_user id)", position = 12)
    @JSONField(ordinal = 12)
    private Integer creator;

    @ApiModelProperty(value = "创建人名称(客户名称 customer company, 用户名称 system_user name)", position = 13)
    @JSONField(ordinal = 13)
    private String creatorName;

    @ApiModelProperty(value = "提交时间(创建时间)", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 14, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submissionTime;

    @ApiModelProperty(value = "操作人(后台用户、用户id)(system_user id)", position = 15)
    @JSONField(ordinal = 15)
    private Long operator;

    @ApiModelProperty(value = "操作人名称(system_user name)", position = 16)
    @JSONField(ordinal = 16)
    private String operatorName;

    @ApiModelProperty(value = "操作时间", position = 17)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 17, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "问题回复(操作人回复)", position = 18)
    @JSONField(ordinal = 18)
    private String revert;

    @ApiModelProperty(value = "评价(创建人评价)", position = 19)
    @JSONField(ordinal = 19)
    private String evaluation;


    @ApiModelProperty(value = "文件附件(url)文件上传", position = 20)
    @JSONField(ordinal = 20)
    private List<TemplateUrlVO> fileUrls;

}
