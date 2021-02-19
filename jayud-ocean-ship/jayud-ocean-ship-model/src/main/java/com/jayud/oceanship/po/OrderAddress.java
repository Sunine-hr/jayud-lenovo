package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单地址表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrderAddress对象", description="订单地址表")
public class OrderAddress extends Model<OrderAddress> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "业务主键(根据类型选择对应表的主键)")
    private Long businessId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "绑定商品id")
    private Long bindGoodsId;

    @ApiModelProperty(value = "类型(0:发货,1:收货,2:通知,3:提货,4:送货)")
    private Integer type;

    @ApiModelProperty(value = "业务类型(0:空运,1:纯报关,2:中港运输...)参考BusinessTypeEnum")
    private Integer businessType;

    @ApiModelProperty(value = "公司名称")
    private String company;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "邮箱")
    private String mailbox;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "附件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "附件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "交货日期(提货日期/送货日期)")
    private LocalDateTime deliveryDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
