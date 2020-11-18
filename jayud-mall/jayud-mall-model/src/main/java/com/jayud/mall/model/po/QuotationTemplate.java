package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "模板类型(1整柜 2散柜)")
    private Integer types;

    @ApiModelProperty(value = "服务分类(service_group sid)")
    private Integer sid;

    @ApiModelProperty(value = "报价名")
    private String names;

    @ApiModelProperty(value = "报价图片，多张用逗号分割")
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "起运港")
    private String startShipment;

    @ApiModelProperty(value = "目的港")
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔")
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(custome.id，多客户时逗号分隔用户ID)")
    private String visibleUid;

    @ApiModelProperty(value = "开船日期")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔")
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔")
    private String areaId;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔")
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)")
    private Integer taskId;

    @ApiModelProperty(value = "操作信息")
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
