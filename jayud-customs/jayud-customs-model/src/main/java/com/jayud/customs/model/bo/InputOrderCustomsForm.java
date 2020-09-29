package com.jayud.customs.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Data
public class InputOrderCustomsForm {

    @ApiModelProperty(value = "通关口岸code",required = true)
    @NotEmpty(message = "portCode is required")
    private String portCode;

    @ApiModelProperty(value = "通关口岸",required = true)
    @NotEmpty(message = "portName is required")
    private String portName;

    @ApiModelProperty(value = "货物流向",required = true)
    @NotEmpty(message = "goodsType is required")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址")
    private String cntrPic;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cntrPics = new ArrayList<>();

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "六联单号附件")
    private String encodePic;

    @ApiModelProperty(value = "六联单号附件数组集合")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递)")
    private String bizModel;

    @ApiModelProperty(value = "提运单")
    private String airTransportNo;

    @ApiModelProperty(value = "提运单附件")
    private String airTransportPic;

    @ApiModelProperty(value = "提运单附件数组集合")
    private List<FileView> airTransportPics = new ArrayList<>();

    @ApiModelProperty(value = "提运单号")
    private String seaTransportNo;

    @ApiModelProperty(value = "提运单号附件")
    private String seaTransportPic;

    @ApiModelProperty(value = "提运单号附件数组集合")
    private List<FileView> seaTransportPics = new ArrayList<>();

    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
    private String isAgencyTax;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "子订单",required = true)
    @NotEmpty(message = "subOrders is required")
    private List<InputSubOrderCustomsForm> subOrders;


}
