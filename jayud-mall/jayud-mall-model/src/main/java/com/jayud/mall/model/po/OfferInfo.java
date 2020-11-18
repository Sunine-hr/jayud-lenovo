package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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

    @ApiModelProperty(value = "自增加id")
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)")
    private Integer qie;

    @ApiModelProperty(value = "报价名称")
    private String names;

    @ApiModelProperty(value = "开船日期")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "类型1整柜 2散柜")
    private Integer types;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "预计到达时间")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "操作信息")
    private String remarks;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
