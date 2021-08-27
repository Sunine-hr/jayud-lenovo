package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
public class HgTruckLicensePlateForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "运输公司ID")
    @NotNull(message = "运输公司不为空")
    private Integer truckCompanyId;

    @ApiModelProperty(value = "运输公司")
    private String truckCompany;

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

    @ApiModelProperty(value = "备注")
    private String remark;
}
