package com.jayud.oceanship.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.oceanship.po.SeaReplenishment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 海运节点操作流程
 */
@Data
@Slf4j
public class SeaProcessOptForm {

    @NotNull(message = "主订单id不能为空")
    @ApiModelProperty(value = "主订单id", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "海运订单号", required = true)
    private String orderNo;

    @NotNull(message = "海运订单id不能为空")
    @ApiModelProperty(value = "海运订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "订船信息")
    private AddSeaBookShipForm seaBookShipForm = new AddSeaBookShipForm();

    @ApiModelProperty(value = "海外代理公司id")
    private Long agentSupplierId;

    @ApiModelProperty(value = "代理服务类型（0:清关,1:配送")
    private List<Long> proxyServiceType;


    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

//    @ApiModelProperty(value = "状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)")
//    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureName;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationName;

    @ApiModelProperty(value = "柜型大小id")
    private Integer cabinetSize;

    @ApiModelProperty(value = "柜型类型id")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "货好时间")
    private String goodTime;

    @ApiModelProperty(value = "运费是否到付(1代表true,0代表false)")
    private Boolean isFreightCollect;

    @ApiModelProperty(value = "其他费用是否到付(1代表true,0代表false)")
    private Boolean isOtherExpensesPaid;

    @ApiModelProperty(value = "是否危险品(1代表true,0代表false)")
    private Boolean isDangerousGoods;

    @ApiModelProperty(value = "是否带电(1代表true,0代表false)")
    private Boolean isCharged;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "发货地址集合")
    private List<AddOrderAddressForm> deliveryAddress;

    @ApiModelProperty(value = "收货地址集合")
    private List<AddOrderAddressForm> shippingAddress;

    @ApiModelProperty(value = "通知地址集合")
    private List<AddOrderAddressForm> notificationAddress;

    @ApiModelProperty(value = "海运订单地址信息")
    private List<AddOrderAddressForm> orderAddressForms;

    @ApiModelProperty(value = "货品信息")
    private List<AddGoodsForm> goodsForms;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    @ApiModelProperty(value = "结算部门")
    private Long unitDepartmentId;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "合并或者分单(合并为1，分单为2)")
    private Integer type;

    @ApiModelProperty(value = "需要补料的订单数据")
    private List<AddSeaOrderForm> seaOrderForms;

    @ApiModelProperty(value = "补料信息集合")
    private List<AddSeaReplenishment> seaReplenishments;

    /**
     * 校验创建海运子订单参数
     */
    public boolean checkCreateOrder() {
        //海运
        if (this.legalEntityId == null || StringUtils.isEmpty(this.unitCode)
                || this.impAndExpType == null || this.terms == null
                || StringUtils.isEmpty(this.portDepartureCode)
                || StringUtils.isEmpty(this.portDestinationCode)
                || this.goodTime == null) {
            return false;
        }
        // 发货/收货地址是必填项
        if (CollectionUtils.isEmpty(this.deliveryAddress)) {
            log.warn("发货地址信息不能为空");
            return false;
        }
        if (CollectionUtils.isEmpty(this.shippingAddress)) {
            log.warn("收货地址信息不能为空");
            return false;
        }
        if (this.notificationAddress.size() == 0 ||
                StringUtils.isEmpty(this.notificationAddress.get(0).getAddress())) {
            this.notificationAddress = null;
        }

        //货品信息
        for (AddGoodsForm goodsForm : goodsForms) {
            if (!goodsForm.checkCreateAirOrder()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 拼装地址
     */
    public void assemblyAddress() {
        this.orderAddressForms = new ArrayList<>();
        this.orderAddressForms.addAll(this.deliveryAddress);
        this.orderAddressForms.addAll(this.shippingAddress);
        if (CollectionUtils.isNotEmpty(this.notificationAddress)
                && StringUtils.isNotEmpty(this.notificationAddress.get(0).getAddress())) {
            this.orderAddressForms.addAll(this.notificationAddress);
        }
    }


    @ApiModelProperty(value = "业务类型(0:空运),(1,纯报关),(2,中港运输) 前端不用管")
    private Integer businessType;

    @ApiModelProperty(value = "状态码,前台不用传")
    private String status;

    @ApiModelProperty(value = "状态名称,前台不用传")
    private String statusName;

    @ApiModelProperty(value = "附件,前台不用传")
    private String statusPic;

    @ApiModelProperty(value = "附件名称,前台不用传")
    private String statusPicName;

    public void setStatus(String status) {
        this.status = status;
        this.statusName = OrderStatusEnum.getDesc(status);
    }

    public void checkProcessOpt(OrderStatusEnum statusEnum) {
        boolean pass = true;
        switch (statusEnum) {
            case SEA_S_1: //海运接单
            case SEA_S_3: //确认订单入仓
            case SEA_S_5: //提单草稿确认
                pass = checkOptInfo();
                break;
            case SEA_S_2: //订船
                pass = this.seaBookShipForm.checkBookShipParam();
                break;
            case SEA_S_6: //确认装船
                pass = this.seaBookShipForm.checkBookShipOptParam();
                break;
            case SEA_S_7://放单确认
                if (CollectionUtils.isEmpty(this.fileViewList)) throw new JayudBizException("提单文件必传", 400);
//                if (this.fileViewList.size() != 1) throw new JayudBizException("只能上传一个提单文", 400);
                if (!checkOptInfo()) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
                pass = this.seaBookShipForm.checkConfirmLadingBillOptParam();
                break;
            case SEA_S_8://确认到港
                pass = this.seaBookShipForm.checkConfirmATAOptParam();
                break;
            case SEA_S_9://海外代理
                if (!checkOptInfo()) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
                pass = this.checkOverseasAgencyOpt();
                //pass = this.checkOptInfo();
                break;
            case SEA_S_10://订单签收
                if (CollectionUtils.isEmpty(this.fileViewList)) throw new JayudBizException("上传签收单必传", 400);
//                if (this.fileViewList.size() != 1) throw new JayudBizException("只能上传一个提单文", 400);
                if (!checkOptInfo()) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
                break;
        }
        if (!pass) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
    }


    public boolean checkOverseasAgencyOpt() {
        String title = "海外代理";
        if (this.agentSupplierId == null) {
            log.warn(title + " 海外代理id必填");
            return false;
        }
//        if (this.proxyServiceType == null) {
//            log.warn(title + " 代理服务必填");
//            return false;
//        }
        return true;
    }

    public boolean checkOptInfo() {
        if (StringUtils.isEmpty(this.operatorUser)) {
            log.warn("操作人必填");
            return false;
        }
        if (StringUtils.isEmpty(this.operatorTime)) {
            log.warn("操作时间必填");
            return false;
        }
        return true;
    }
}
