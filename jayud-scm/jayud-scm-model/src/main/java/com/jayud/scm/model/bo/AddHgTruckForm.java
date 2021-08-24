package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 港车运输主表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddHgTruckForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务类型")
    @NotNull(message = "业务类型不为空")
    private Integer modelType;

    @ApiModelProperty(value = "车次编号")
    private String truckNo;

    @ApiModelProperty(value = "车次日期")
    @NotNull(message = "车次时间不为空")
    private LocalDateTime truckDate;

    @ApiModelProperty(value = "车次时间(上午9：00，下午6：00等)")
    private String truckTime;

    @ApiModelProperty(value = "运输公司ID")
    private Integer truckCompanyId;

    @ApiModelProperty(value = "运输公司")
    @NotNull(message = "运输公司不为空")
    private String truckCompany;

    @ApiModelProperty(value = "车辆类型（0正常，1：加车，2包车）")
    @NotNull(message = "车辆类型不为空")
    private Integer sType;

    @ApiModelProperty(value = "预订车次类型")
    @NotNull(message = "车次类型不为空")
    private String preTruckStyle;

    @ApiModelProperty(value = "起运地")
    private String origin;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "大陆车牌")
    private String cnTruckNo;

    @ApiModelProperty(value = "香港车牌")
    private String hkTruckNo;

    @ApiModelProperty(value = "车次类型")
    private String truckType;

    @ApiModelProperty(value = "司机姓名")
    @NotNull(message = "司机姓名不为空")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverTel;

    @ApiModelProperty(value = "进/出口岸")
    private String outPort;

    @ApiModelProperty(value = "载货清单")
    @NotNull(message = "载货清单不为空")
    private String exHkNo;

    @ApiModelProperty(value = "车辆编号")
    private String regNo;

    @ApiModelProperty(value = "海关编码")
    private String hsCode;

    @ApiModelProperty(value = "币别")
    @NotNull(message = "币别不为空")
    private String currencyName;

    @ApiModelProperty(value = "实付款运费")
    private String realCost;

    @ApiModelProperty(value = "运输费用")
    private BigDecimal cost;

    @ApiModelProperty(value = "是否紧急车次")
    private Integer urgently;

    @ApiModelProperty(value = "是否购买保险")
    private Integer isSafe;

    @ApiModelProperty(value = "状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "封条号")
    private String lockNum;

    @ApiModelProperty(value = "封条颜色")
    private String lockColour;

    @ApiModelProperty(value = "车次异常说明")
    private String diffRemark;

    @ApiModelProperty(value = "异常类型（正常，押夜，查车）")
    private String diffType;

    @ApiModelProperty(value = "柜车号码")
    @NotNull(message = "柜车号码不为空")
    private String cabinetNo;

    @ApiModelProperty(value = "自动出库标志")
    @NotNull(message = "自动完成出库不为空")
    private BigDecimal autoShippingFlag;

    @ApiModelProperty(value = "出库目标仓库")
    @NotNull(message = "出库目标仓库不为空")
    private String shippingHubName;

    @ApiModelProperty(value = "出库目标仓库id")
    private Integer shippingHubId;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

}
