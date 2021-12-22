package com.jayud.tools.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * FBA订单
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Data
public class FbaOrderVO {

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
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
