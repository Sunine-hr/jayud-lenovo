package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商对应车辆信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "VehicleInfo对象", description = "供应商对应车辆信息")
public class VehicleInfo extends Model<VehicleInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "香港车牌")
    private String hkNumber;

    @ApiModelProperty(value = "供应商id(supplier_info id)")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名字(supplier_info name)")
    private String supplierName;

    @ApiModelProperty(value = "牌头公司")
    private String ptCompany;

    @ApiModelProperty(value = "牌头传真")
    private String ptFax;

    @ApiModelProperty(value = "车辆类型(1吨车 2柜车)")
    private Integer carType;

    @ApiModelProperty(value = "牌头电话")
    private String ptPhone;

    @ApiModelProperty(value = "海关编码")
    private String customsCode;

    @ApiModelProperty(value = "通关卡号")
    private String cardNumber;

    @ApiModelProperty(value = "吉车重量")
    private String weight;

    @ApiModelProperty(value = "附件,多个时用逗号分隔")
    private String files;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "寮步密码")
    private String steppingCode;

    @ApiModelProperty(value = "企业代码")
    private String enterpriseCode;

    @ApiModelProperty(value = "车辆吨位")
    private String vehicleTonnage;

    @ApiModelProperty(value = "附件名称，多个用逗号隔开")
    private String fileName;

    @ApiModelProperty(value = "司机id(多个用,隔开)")
    private String driverInfoIds;

    @ApiModelProperty(value = "主司机id")
    private Long mainDriverId;

    @ApiModelProperty(value = "车辆类型(0:中港车,1:内陆车)")
    private Integer type;

    @ApiModelProperty(value = "保险公司")
    private String insuranceCompany;

    @ApiModelProperty(value = "是否保险")
    private Boolean isInsured;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "车架号")
    private String frameNum;

    @ApiModelProperty(value = "最近一次年审日期")
    private LocalDateTime lastAnnualReviewDate;

    @ApiModelProperty(value = "下次年审日期")
    private LocalDateTime nextAnnualReviewDate;

    @ApiModelProperty(value = "购置日期")
    private LocalDateTime purchaseDate;

    @ApiModelProperty(value = "最近一次季审日期")
    private LocalDateTime lastQuarterlyReviewDate;

    @ApiModelProperty(value = "下一次季审日期")
    private LocalDateTime nextQuarterlyReviewDate;

    @ApiModelProperty(value = "商业险到期日期")
    private LocalDateTime commercialInsuranceExpiration;

    @ApiModelProperty(value = "强制险到期日期")
    private LocalDateTime compulsoryInsuranceDateExpiry;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
