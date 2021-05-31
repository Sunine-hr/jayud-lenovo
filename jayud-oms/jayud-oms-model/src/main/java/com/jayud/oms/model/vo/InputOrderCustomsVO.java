package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Data
public class InputOrderCustomsVO {

    @ApiModelProperty(value = "报关订单id")
    private Long id;

    @ApiModelProperty(value = "通关口岸code")
    private String portCode;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "货物流向")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cntrPics = new ArrayList<>();

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "六联单号附件数组集合")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递)")
    private String bizModel;

    @ApiModelProperty(value = "提运单")
    private String airTransportNo;

    @ApiModelProperty(value = "提运单附件数组集合")
    private List<FileView> airTransportPics = new ArrayList<>();

    @ApiModelProperty(value = "提运单号")
    private String seaTransportNo;

    @ApiModelProperty(value = "提运单号附件数组集合")
    private List<FileView> seaTransportPics = new ArrayList<>();

    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
    private String isAgencyTax;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "报关单数")
    private String number;

    @ApiModelProperty(value = "子订单")
    private List<InputSubOrderCustomsVO> subOrders = new ArrayList<>();

    @ApiModelProperty(value = "所有附件集合")
    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "审核状态")
    private String subCustomsStatus;

    @ApiModelProperty(value = "审核状态")
    private String status;

    @ApiModelProperty(value = "监管方式")
    private String supervisionMode;

    @ApiModelProperty(value = "订单备注")
    private String orderRemarks;


    public void setSubCustomsStatus(String subCustomsStatus) {
        this.subCustomsStatus = subCustomsStatus;
        this.status = subCustomsStatus;
    }

    public void copyOperationInfo() {
        this.id = null;
        this.cntrPics = new ArrayList<>();
        this.encode = null;
        this.encodePics = new ArrayList<>();
        this.airTransportPics = new ArrayList<>();
        this.seaTransportPics = new ArrayList<>();
        this.allPics = new ArrayList<>();
        this.subOrders = null;
        this.number = null;
        this.subCustomsStatus = null;
        this.status = null;
//        if (CollectionUtils.isNotEmpty(this.subOrders)) {
//            subOrders.forEach(e -> {
//                e.setSubOrderId(null);
//                e.setFileViews(null);
//                e.setYunCustomsNo(null);
//                e.setJiedanTimeStr(null);
//                e.setJiedanUser(null);
//                e.setOrderNo(null);
//            });
//        }
    }
}
