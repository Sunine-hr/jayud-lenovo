package com.jayud.oms.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InputOrderTransportForm {

    @ApiModelProperty(value = "中港订单ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "中港订单号,修改时传")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "通关口岸code",required = true)
    private String portCode;

    @ApiModelProperty(value = "货物流向",required = true)
    private Integer goodsType;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "车型(1吨车 2柜车)",required = true)
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)",required = true)
    private Integer vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址,前台忽略")
    private String cntrPic;

    @ApiModelProperty(value = "柜号上传附件地址名称,前台忽略")
    private String cntrPicName;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cntrPics = new ArrayList<>();

    @ApiModelProperty(value = "中转仓库ID",required = true)
    private Long warehouseInfoId;

    @ApiModelProperty(value = "1-装货 0-不需要装货")
    private String isLoadGoods;

    @ApiModelProperty(value = "1-卸货 0-不需要卸货")
    private String isUnloadGoods;

    @ApiModelProperty(value = "接单法人",required = true)
    private String legalName;

    @ApiModelProperty(value = "结算单位",required = true)
    private String unitCode;

    @ApiModelProperty(value = "香港清关结算单位,选择了香港清关必填")
    private String hkUnitCode;

    @ApiModelProperty(value = "香港清关接单法人,选择了香港清关必填")
    private String hkLegalName;

    @ApiModelProperty(value = "是否香港清关 1-是 0-否,选择了香港清关必填")
    private String isHkClear;

    @ApiModelProperty(value = "提货地址")
    private List<InputOrderTakeAdrForm> takeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "送货地址")
    private List<InputOrderTakeAdrForm> takeAdrForms2 = new ArrayList<>();

    @ApiModelProperty(value = "当前登录用户,FeignClient必传,要么就传token,否则跨系统拿不到用户")
    private String loginUser;

    @ApiModelProperty(value = "审核状态")
    private String subTmsStatus;

}
