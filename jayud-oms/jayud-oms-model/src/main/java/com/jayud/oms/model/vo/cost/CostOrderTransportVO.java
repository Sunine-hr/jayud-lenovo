package com.jayud.oms.model.vo.cost;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.vo.InputOrderTakeAdrVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CostOrderTransportVO {

    @ApiModelProperty(value = "中港订单号")
    private String orderNo;

//    @ApiModelProperty(value = "接单人")
//    private String jiedanUser;
//
//    @ApiModelProperty(value = "接单时间")
//    private String jiedanTimeStr;

//    @ApiModelProperty(value = "货物流向描述")
//    private String goodsTypeDesc;

    @ApiModelProperty("接单法人")
    private String legalName;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

//    @ApiModelProperty(value = "六联单号")
//    private String encode;


//    @ApiModelProperty(value = "香港车牌")
//    private String hkLicensePlate;
//
//    @ApiModelProperty(value = "大陆车牌")
//    private String licensePlate;

    @ApiModelProperty(value = "车型(3T)")
    private String vehicleSize;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;

//    @ApiModelProperty(value = "1-装货 0-不需要装货")
//    private String isLoadGoods;
//
//    @ApiModelProperty(value = "1-卸货 0-不需要卸货")
//    private String isUnloadGoods;

//    @ApiModelProperty(value = "柜号")
//    private String cntrNo;

//    @ApiModelProperty(value = "车型/柜号")
//    private String modelAndCntrNo;

//    @ApiModelProperty(value = "运输公司")
//    private String supplierChName;

//    @ApiModelProperty(value = "海关编号,预留字段")
//    private String customsNo;

//    @ApiModelProperty(value = "司机姓名")
//    private String driverName;
//
//    @ApiModelProperty(value = "司机大陆电话")
//    private String driverPhone;
//
//    @ApiModelProperty(value = "司机香港电话")
//    private String driverHkPhone;
//
//    @ApiModelProperty(value = "过磅数")
//    private String carWeighNum;
//
//    @ApiModelProperty(value = "总件数")
//    private Integer totalAmount;
//
//    @ApiModelProperty(value = "总重量")
//    private Integer totalWeight;
//
//    @ApiModelProperty(value = "无缝单号")
//    private String seamlessNo;
//
//    @ApiModelProperty(value = "清关单号")
//    private String clearCustomsNo;
//
//    @ApiModelProperty(value = "装车要求")
//    private String remarks;

//    @ApiModelProperty(value = "柜号上传附件地址,前台忽略")
//    private String cntrPic;
//
//    @ApiModelProperty(value = "柜号上传附件地址名称,前台忽略")
//    private String cntrPicName;

//    @ApiModelProperty(value = "所有附件信息")
//    private List<FileView> allPics = new ArrayList<>();

//    @ApiModelProperty(value = "香港清关接单法人")
//    private String hkLegalName;

//    @ApiModelProperty(value = "是否香港清关")
//    private String isHkClear;

//    @ApiModelProperty(value = "是否车辆过磅")
//    private Boolean isVehicleWeigh;

    //中转信息
//    @ApiModelProperty(value = "中转公司")
//    private String companyName;

//    @ApiModelProperty(value = "联系人")
//    private String contacts;
//
//    @ApiModelProperty(value = "电话")
//    private String contactNumber;
//
//    @ApiModelProperty(value = "地址")
//    private String address;

//    @ApiModelProperty(value = "提货地址")
//    private List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();

//    @ApiModelProperty(value = "卸货地址")
//    private List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();

    @ApiModelProperty(value = "卸货地址")
    private String goodsInfo;


    public void assemblyTakeAdr(List<InputOrderTakeAdrVO> orderTakeAdrForms2) {
        if (CollectionUtil.isEmpty(orderTakeAdrForms2)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms2) {
            sb.append(inputOrderTakeAdrVO.getGoodsDesc())
                    .append("/")
                    .append(inputOrderTakeAdrVO.getPlateAmount()==null?0:inputOrderTakeAdrVO.getPlateAmount()).append("板")
                    .append("/")
                    .append(inputOrderTakeAdrVO.getPieceAmount()).append("件数")
                    .append("/")
                    .append(inputOrderTakeAdrVO.getWeight()).append("kg")
                    .append(",");
        }
        goodsInfo = sb.toString();
    }


//    public void assembleModelAndCntrNo() {
//        String vehicleSize = this.vehicleSize;
//        if (this.vehicleType != null && this.vehicleType == 2) {
//            String cntrNo = StringUtils.isEmpty(this.cntrNo) ? "" : this.cntrNo;
//            vehicleSize = vehicleSize + "/" + cntrNo;
//        }
//        this.modelAndCntrNo = vehicleSize;
//    }
}
