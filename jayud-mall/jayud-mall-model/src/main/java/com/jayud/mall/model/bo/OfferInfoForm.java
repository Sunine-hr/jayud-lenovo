package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value="OfferInfoForm对象", description="报价表单")
public class OfferInfoForm {

    @ApiModelProperty(value = "自增加id", position = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)", position = 2)
    private Integer qie;

    @ApiModelProperty(value = "报价名称", position = 3)
    private String names;

    @ApiModelProperty(value = "开船日期", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "类型1整柜 2散柜", position = 8)
    private Integer types;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 9)
    private String status;

    @ApiModelProperty(value = "创建人id", position = 10)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名", position = 11)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "预计到达时间", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "操作信息", position = 14)
    private String remarks;

}
