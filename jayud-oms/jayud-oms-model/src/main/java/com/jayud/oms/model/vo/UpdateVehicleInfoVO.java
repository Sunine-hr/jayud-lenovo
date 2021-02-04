package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.enums.VehicleTypeEnum;
import com.jayud.oms.model.po.DriverInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商对应车辆信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class UpdateVehicleInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "香港车牌")
    private String hkNumber;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

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

    @ApiModelProperty(value = "附件名称,多个时用逗号分隔")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private String status;

    @ApiModelProperty(value = "寮步密码")
    private String steppingCode;

    @ApiModelProperty(value = "企业代码")
    private String enterpriseCode;

    @ApiModelProperty(value = "车辆吨位")
    private String vehicleTonnage;

    @ApiModelProperty(value = "司机id(多个用,隔开)")
    private String driverInfoIds;

    @ApiModelProperty(value = "司机名称(多个用,隔开)")
    private List<DriverInfo> driverInfos;

    @ApiModelProperty(value = "司机id集合")
    private List<Long> driverIds;

    @ApiModelProperty(value = "主司机id")
    private Long mainDriverId;


    public void setDriverInfoIds(String driverInfoIds) {
        this.driverInfoIds = driverInfoIds;
        List<Long> driverIds = new ArrayList<>();
        for (String driverInfoId : driverInfoIds.split(",")) {
            driverIds.add(Long.valueOf(driverInfoId));
        }
        this.driverIds = driverIds;
    }

}
