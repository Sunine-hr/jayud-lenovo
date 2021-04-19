package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.vo.TemplateUrlVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TradingRecordForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "客户ID(customer id)", position = 2)
    @JSONField(ordinal = 2)
    private Long customerId;

    @ApiModelProperty(value = "交易单号", position = 3)
    @JSONField(ordinal = 3)
    private String tradingNo;

    @ApiModelProperty(value = "交易类型(1充值 2支付)", position = 4)
    @JSONField(ordinal = 4)
    private Integer tradingType;

    @ApiModelProperty(value = "金额", position = 5)
    @JSONField(ordinal = 5)
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    @NotNull(message = "币种不能为空")
    private Long cid;

    @ApiModelProperty(value = "交易流水号", position = 7)
    @TableField("serialNumber")
    @JSONField(ordinal = 7)
    @NotNull(message = "交易流水号不能为空")
    private String serialNumber;

    @ApiModelProperty(value = "交易凭证(url)", position = 8)
    @JSONField(ordinal = 8)
    private String voucherUrl;

    @ApiModelProperty(value = "状态(0待审核 1审核通过 2审核不通过)", position = 9)
    @JSONField(ordinal = 9)
    private String status;

    @ApiModelProperty(value = "交易备注", position = 10)
    @JSONField(ordinal = 10)
    private String remark;

    @ApiModelProperty(value = "创建人(customer id)", position = 11)
    @JSONField(ordinal = 11)
    private Integer creator;

    @ApiModelProperty(value = "创建时间(交易时间)(充值时间)", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "审核人(system_user id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer auditor;

    @ApiModelProperty(value = "审核时间", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 14, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    //模板urlVO
    @ApiModelProperty(value = "交易凭证(url)文件上传", position = 15)
    @JSONField(ordinal = 15)
    private List<TemplateUrlVO> voucherUrls;

}
