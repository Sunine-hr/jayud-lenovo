package com.jayud.airfreight.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 空运订舱表
 * </p>
 *
 * @author LDR
 * @since 2020-12-03
 */
@Data
public class AirBookingVO extends Model<AirBookingVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "状态(0:确认,1:待确认)")
    private String status;

    @ApiModelProperty(value = "空运订单编号")
    private String airOrderNo;

    @ApiModelProperty(value = "空运订单id")
    private Long airOrderId;

    @ApiModelProperty(value = "代理供应商id")
    private Long agentSupplierId;

    @ApiModelProperty(value = "入仓号")
    private String warehousingNo;

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "截关日期")
    private LocalDateTime cutOffDate;

    @ApiModelProperty(value = "航空公司")
    private String airlineCompany;

    @ApiModelProperty(value = "航班")
    private String flight;

    @ApiModelProperty(value = "ETD 预计离港时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime etd;

    @ApiModelProperty(value = "ATD 实际离岗时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atd;

    @ApiModelProperty(value = "ETA 预计到港时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eta;

    @ApiModelProperty(value = "ATA 实际到港时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ata;

    @ApiModelProperty(value = "交仓地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "交仓仓库")
    private String deliveryWarehouse;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "提单重量")
    private Double billLadingWeight;

    @ApiModelProperty(value = "计费重量")
    private Double billingWeight;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
