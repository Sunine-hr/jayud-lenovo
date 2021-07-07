package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报价管理
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OfferInfo对象", description="报价管理")
public class OfferInfo extends Model<OfferInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer qie;

    @ApiModelProperty(value = "报价名称", position = 3)
    @JSONField(ordinal = 3)
    private String names;

    @ApiModelProperty(value = "开船日期", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 4, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "类型1整柜 2拼箱", position = 8)
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
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "操作信息", position = 14)
    @JSONField(ordinal = 14)
    private String remarks;

    @ApiModelProperty(value = "开船日期备注")
    private String sailTimeRemark;

    @ApiModelProperty(value = "特别说明")
    private String specialVersion;

    @ApiModelProperty(value = "报价单号")
    private String offerNo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
