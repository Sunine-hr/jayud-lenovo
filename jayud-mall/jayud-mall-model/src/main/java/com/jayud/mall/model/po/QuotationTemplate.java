package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 报价模板
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="QuotationTemplate对象", description="报价模板")
public class QuotationTemplate extends Model<QuotationTemplate> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "模板类型(1整柜 2拼箱)", position = 2)
    @JSONField(ordinal = 2)
    private Integer types;

    @ApiModelProperty(value = "服务分类(service_group id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer sid;

    @ApiModelProperty(value = "报价模板名", position = 4)
    @JSONField(ordinal = 4)
    private String names;

    @ApiModelProperty(value = "报价图片，多张用逗号分割", position = 5)
    @JSONField(ordinal = 5)
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer tid;

    @ApiModelProperty(value = "起运港(harbour_info id_code)", position = 7)
    @JSONField(ordinal = 7)
    private String startShipment;

    @ApiModelProperty(value = "目的港(harbour_info id_code)", position = 8)
    @JSONField(ordinal = 8)
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔", position = 9)
    @JSONField(ordinal = 9)
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)", position = 10)
    @JSONField(ordinal = 10)
    private Integer visibleCustomer;

    @ApiModelProperty(value = "可见客户(customer.id，多客户时逗号分隔用户ID)", position = 11)
    @JSONField(ordinal = 11)
    private String visibleUid;

    @ApiModelProperty(value = "开船日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 13, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 14, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "预计到达时间", position = 16)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 16, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 17)
    @JSONField(ordinal = 17)
    private Integer gidtype;

    @ApiModelProperty(value = "货物类型(goods_type id),多个用逗号分隔", position = 18)
    @JSONField(ordinal = 18)
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 19)
    @JSONField(ordinal = 19)
    private String areaId;

    @ApiModelProperty(value = "报价类型(1整柜 2拼箱)", position = 20)
    @JSONField(ordinal = 20)
    private Integer qidtype;

    @ApiModelProperty(value = "报价类型(quotation_type id),多个用逗号分隔", position = 21)
    @JSONField(ordinal = 21)
    private String qid;

    @ApiModelProperty(value = "任务分组id(运单任务task_group id)", position = 22)
    @JSONField(ordinal = 22)
    private Integer taskId;

    @ApiModelProperty(value = "计泡系数(默认6000)", position = 23)
    @JSONField(ordinal = 23)
    private BigDecimal bubbleCoefficient;

    @ApiModelProperty(value = "操作信息", position = 24)
    @JSONField(ordinal = 24)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 25)
    @JSONField(ordinal = 25)
    private String status;

    @ApiModelProperty(value = "创建人id(system_user id)", position = 26)
    @JSONField(ordinal = 26)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名(system_user name)", position = 27)
    @JSONField(ordinal = 27)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 28)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 28, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", position = 29)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 29, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "计费重单位(1柜 2KG 3CBM 4车)")
    private Integer billingWeightUnit;

    @ApiModelProperty(value = "整体时效")
    private String wholeTime;

    @ApiModelProperty(value = "计算公式\n" +
            "1材积->重量：长*高*宽/计泡系数(单位KG)\n" +
            "2重量->材积：实重/计泡系数(单位CBM)")
    private Integer designFormulas;

    @ApiModelProperty(value = "容量(数值)")
    private BigDecimal volume;

    @ApiModelProperty(value = "容量单位(1KG 2CBM)")
    private Integer volumeUnit;

    @ApiModelProperty(value = "最低数量")
    private BigDecimal minimumQuantity;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
