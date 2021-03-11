package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(value="OfferInfoForm对象", description="报价表单")
public class OfferInfoForm {

    @ApiModelProperty(value = "自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template id)", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "报价模板id不能为空")
    private Integer qie;

    @ApiModelProperty(value = "报价名称", position = 3)
    @JSONField(ordinal = 3)
    @NotNull(message = "报价名称不能为空")
    private String names;

    @ApiModelProperty(value = "开船日期", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 4, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开船日期不能为空")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "截单日期不能为空")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "截仓日期不能为空")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "截亏仓日期不能为空")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "类型1整柜 2散柜", position = 8)
    @JSONField(ordinal = 8)
    private Integer types;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 9)
    @JSONField(ordinal = 9)
    private String status;

    @ApiModelProperty(value = "创建人id(system_user id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名(system_user name)", position = 11)
    @JSONField(ordinal = 11)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "预计到达时间", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 13, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "预计到达时间不能为空")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "操作信息", position = 14)
    @JSONField(ordinal = 14)
    private String remarks;

}
