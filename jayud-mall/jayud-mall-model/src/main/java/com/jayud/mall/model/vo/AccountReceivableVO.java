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
public class AccountReceivableVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "对账单编号", position = 2)
    @JSONField(ordinal = 2)
    private String dzdNo;

    @ApiModelProperty(value = "法人主体id(legal_person id)", position = 3)
    @JSONField(ordinal = 3)
    private Long legalPersonId;

    @ApiModelProperty(value = "客户ID(customer id)", position = 4)
    @JSONField(ordinal = 4)
    private Integer customerId;

    @ApiModelProperty(value = "账期开始时间", position = 5)
    @JSONField(ordinal = 5)
    private LocalDateTime paymentDaysStart;

    @ApiModelProperty(value = "账期结束时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDaysEnd;

    @ApiModelProperty(value = "状态(0待核销 1核销完成)", position = 7)
    @JSONField(ordinal = 7)
    private Integer status;

    @ApiModelProperty(value = "制单时间", position = 8)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 8, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //金额(虚拟字段)
    @ApiModelProperty(value = "应收金额(前端：对账单金额、对账单总金额)", position = 9)
    @JSONField(ordinal = 9)
    private List<String> receivableAmount;

    @ApiModelProperty(value = "结算金额", position = 10)
    @JSONField(ordinal = 10)
    private List<String> balanceAmount;

    @ApiModelProperty(value = "已收金额(前端：已付款金额)", position = 11)
    @JSONField(ordinal = 11)
    private List<String> receivedAmount;

    @ApiModelProperty(value = "未收金额(前端：待付款金额)", position = 12)
    @JSONField(ordinal = 12)
    private List<String> uncalledAmount;

    //法人主体名称
    @ApiModelProperty(value = "法人主体名称", position = 13)
    @JSONField(ordinal = 13)
    private String legalEntity;

    //客户名称
    @ApiModelProperty(value = "客户名称", position = 14)
    @JSONField(ordinal = 14)
    private String customerName;

    //账期
    @ApiModelProperty(value = "账期", position = 15)
    @JSONField(ordinal = 15)
    private String paymentDays;

    @ApiModelProperty(value = "客户账户余额（多币种）", position = 16)
    @JSONField(ordinal = 16)
    private List<String> accountBalanceList;

    //对账单关联的账单
    @ApiModelProperty(value = "对账单关联的账单(应收账单list)", position = 17)
    @JSONField(ordinal = 17)
    private List<ReceivableBillMasterVO> receivableBillMasterVOS;

}
