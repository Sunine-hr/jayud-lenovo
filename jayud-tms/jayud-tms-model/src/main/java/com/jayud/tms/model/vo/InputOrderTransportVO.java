package com.jayud.tms.model.vo;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
public class InputOrderTransportVO {

    @ApiModelProperty(value = "中港订单ID")
    private Long id;

    @ApiModelProperty(value = "中港订单号")
    private String orderNo;

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单时间")
    private String jiedanTimeStr;

    @ApiModelProperty(value = "货物流向")
    private Integer goodsType;

    @ApiModelProperty(value = "货物流向描述")
    private String goodsTypeDesc;

    @ApiModelProperty("通关口岸CODE")
    private String portCode;

    @ApiModelProperty("结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "骑师姓名")
    public String jockey;

    @ApiModelProperty("接单法人")
    private String legalName;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "香港车牌")
    private String hkLicensePlate;

    @ApiModelProperty(value = "大陆车牌")
    private String licensePlate;

    @ApiModelProperty(value = "车型(3T)")
    private String vehicleSize;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;
    @ApiModelProperty(value = "派车单号")
    public String transportNo;

    @ApiModelProperty(value = "中转仓库ID")
    private Long warehouseInfoId;
    @ApiModelProperty("结算单位名称")
    private String unitName;

    @ApiModelProperty(value = "1-装货 0-不需要装货")
    private String isLoadGoods;
    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private String vehicleTypeDesc;

    @ApiModelProperty(value = "1-卸货 0-不需要卸货")
    private String isUnloadGoods;
    @ApiModelProperty(value = "中转仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;
    @ApiModelProperty(value = "装货")
    private String loadGoodsDesc;

    @ApiModelProperty(value = "海关编号,预留字段")
    private String customsNo;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "司机香港电话")
    private String driverHkPhone;

    @ApiModelProperty(value = "过磅数")
    private String carWeighNum;

    @ApiModelProperty(value = "总件数")
    private Integer totalAmount;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "无缝单号")
    private String seamlessNo;

    @ApiModelProperty(value = "清关单号")
    private String clearCustomsNo;

    @ApiModelProperty(value = "装车要求")
    private String remarks;

    @ApiModelProperty(value = "柜号上传附件地址,前台忽略")
    private String cntrPic;

    @ApiModelProperty(value = "柜号上传附件地址名称,前台忽略")
    private String cntrPicName;

    @ApiModelProperty(value = "所有附件信息")
    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "香港清关接单法人")
    private String hkLegalName;

    @ApiModelProperty(value = "香港清关接单法人ID")
    private Long hkLegalId;

    @ApiModelProperty(value = "香港清关结算单位")
    private String hkUnitCode;

    @ApiModelProperty(value = "是否香港清关")
    private String isHkClear;

    @ApiModelProperty(value = "是否车辆过磅")
    private Boolean isVehicleWeigh;

    @ApiModelProperty(value = "卸货")
    private String unloadGoodsDesc;

    //中转信息
    @ApiModelProperty(value = "中转公司")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "电话")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "提货地址")
    private List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "卸货地址")
    private List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();

    @ApiModelProperty(value = "审核状态")
    private String subTmsStatus;

    @ApiModelProperty(value = "指派供应车型尺寸")
    private String supplierVehicleSize;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "供应商名称(运输公司)")
    private String supplierChName;

    @ApiModelProperty(value = "对接GPS所需要的key值")
    private String appKey;

    @ApiModelProperty(value = "对接GPS公用路径前缀")
    private String gpsAddress;

    @ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    @ApiModelProperty(value = "是否车辆过磅描述")
    private String isVehicleWeighDesc;

    @ApiModelProperty(value = "操作部门")
    private String departmentName;

    @ApiModelProperty(value = "六联单号")
    private String encode;

//    @ApiModelProperty(value = "装车要求")
//    private String sendCarRemarks;

    @ApiModelProperty(value = "报关六联单号附件")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "舱单附件")
    private List<FileView> manifestAttachment = new ArrayList<>();

    @ApiModelProperty(value = "报关单附件")
    private List<FileView> customsOrderAttachment = new ArrayList<>();

    @ApiModelProperty(value = "通关时间")
    private String goCustomsTime;

    @ApiModelProperty(value = "派车备注")
    private String sendCarDescribes;

    @ApiModelProperty(value = "通关备注")
    private String goCustomsRemarks;

    public String getSubTmsStatus() {
        return subTmsStatus = this.status;
    }

    public String getVehicleTypeDesc() {
        if (this.vehicleType != null) {
            if (this.vehicleType == 1) {
                return "吨车";
            } else if (this.vehicleType == 2) {
                return "柜车";
            }
        }
        return "";
    }

    public String getGoodsTypeDesc() {
        if (this.goodsType != null) {
            if (this.goodsType == 1) {
                return "进口";
            } else if (this.goodsType == 2) {
                return "出口";
            }
        }
        return "";
    }

    public void setIsVehicleWeigh(Boolean isVehicleWeigh){
        this.isVehicleWeigh = isVehicleWeigh;
        this.isVehicleWeighDesc = isVehicleWeigh ? "是" : "否";
    }


    public void assemblySendCars(OrderSendCarsVO orderSendCars) {
        this.supplierChName = orderSendCars.getSupplierName();
        this.licensePlate = orderSendCars.getPlateNumber();
        this.hkLicensePlate = orderSendCars.getHkNumber();
        this.driverPhone = orderSendCars.getDriverPhone();
        this.driverName = orderSendCars.getDriverName();
        this.jockey = orderSendCars.getJockey();
        this.transportNo = orderSendCars.getTransportNo();
        this.remarks = orderSendCars.getRemarks();
        this.sendCarDescribes=orderSendCars.getDescribes();
    }

    public void assemblyCustomerInfo(Object customerInfos) {
        if (customerInfos == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(customerInfos);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            this.unitName = jsonObject.getStr("name");
        }

    }

    public void assemblyWarehouseInfo(Map<Long, Map<String, Object>> warehouseInfoMap) {
        if (warehouseInfoMap == null) {
            return;
        }
        Map<String, Object> map = warehouseInfoMap.get(this.warehouseInfoId);
        this.warehouseName = MapUtil.getStr(map, "warehouseName");
    }

    public void assemblyMainOrderInfos(Object mainOrderInfos) {
        if (mainOrderInfos == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(mainOrderInfos);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.getStr("");
        }

    }

    public void setIsLoadGoods(String isLoadGoods) {
        this.isLoadGoods = isLoadGoods;
        this.loadGoodsDesc = "1".equals(isLoadGoods) ? "装货" : "";
    }


    public void setIsUnloadGoods(String isUnloadGoods) {
        this.isUnloadGoods = isUnloadGoods;
        this.unloadGoodsDesc = "1".equals(unloadGoodsDesc) ? "卸货" : "";
    }

    /**
     * 报关资料
     *
     * @param customsInfos
     */
    public void assemblyCustomsInfo(Object customsInfos) {
        if (customsInfos == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject(customsInfos);
        this.encode = jsonObject.getStr("encode");
        this.manifestAttachment = jsonObject.getJSONArray("manifestAttachment").toList(FileView.class);
        this.customsOrderAttachment = jsonObject.getJSONArray("customsOrderAttachment").toList(FileView.class);
    }
}
