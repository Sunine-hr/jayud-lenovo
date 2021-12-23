package com.jayud.tools.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * FBA订单
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FbaOrder对象", description="FBA订单")
public class FbaOrder extends Model<FbaOrder> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "客户单号")
    private String customerNo;

    @ApiModelProperty(value = "转运单号")
    private String transshipmentNo;

    @ApiModelProperty(value = "收件日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiptDate;

    @ApiModelProperty(value = "业务员")
    private String salesMan;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "起运港")
    private String portDeparture;

    @ApiModelProperty(value = "目的港")
    private String portDestination;

    @ApiModelProperty(value = "航次")
    private String voyageNumber;

    @ApiModelProperty(value = "件数")
    private Integer number;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
