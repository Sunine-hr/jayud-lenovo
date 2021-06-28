package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海运订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@Slf4j
public class InputSeaOrderForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "海运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long orderId;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "海运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)")
    private String status;

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

    @ApiModelProperty(value = "中转港代码")
    private String transitPortCode;

    @ApiModelProperty(value = "柜型大小")
    private Integer cabinetSize;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜型数量")
    private List<AddCabinetSizeNumber> cabinetSizeNumbers;

    @ApiModelProperty(value = "货好时间")
    private String goodTime;

    @ApiModelProperty(value = "运费是否到付(1代表true,0代表false)")
    private Boolean isFreightCollect = false;

    @ApiModelProperty(value = "其他费用是否到付(1代表true,0代表false)")
    private Boolean isOtherExpensesPaid = false;

    @ApiModelProperty(value = "是否危险品(1代表true,0代表false)")
    private Boolean isDangerousGoods = false;

    @ApiModelProperty(value = "是否带电(1代表true,0代表false)")
    private Boolean isCharged = false;

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

    @ApiModelProperty(value = "附件集合信息")
    private List<FileView> fileViews;

    @ApiModelProperty(value = "文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "截关时间")
    private String closingTime;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "截仓时间")
    private String cutOffTime;

    @ApiModelProperty(value = "代理人地址集合")
    private List<AddOrderAddressForm> agentAddress;

    @ApiModelProperty(value = "操作部门")
    private Long departmentId;

    @ApiModelProperty(value = "发货地")
    private String placeOfDelivery;



    /**
     * 校验创建海运子订单参数
     */
    public String checkCreateOrder() {
        //海运
        String message = null ;
        if (this.legalEntityId == null || StringUtils.isEmpty(this.unitCode)
                || this.impAndExpType == null || this.terms == null
                || StringUtils.isEmpty(this.portDepartureCode)
                || StringUtils.isEmpty(this.portDestinationCode)
                || this.goodTime == null || this.closingTime == null) {
            message = "参数不正确";
        }
        if(CollectionUtils.isNotEmpty(this.deliveryAddress)){
            if(this.deliveryAddress.get(0).getAddress().length() > 1000){
                message = "发货人信息不超过1000个字符";
            }
        }
        if(CollectionUtils.isNotEmpty(this.shippingAddress)){
            if(this.shippingAddress.get(0).getAddress().length() > 1000){
                message = "收货人信息不超过1000个字符";
            }
        }
        if(CollectionUtils.isNotEmpty(this.notificationAddress)){
            if(notificationAddress.get(0).getAddress().length() > 1000){
                message = "通知人信息不超过1000个字符";
            }
        }
        if(CollectionUtils.isNotEmpty(this.agentAddress)){
            if(agentAddress.get(0).getAddress().length() > 1000){
                message = "代理人信息不超过1000个字符";
            }
        }
        if(CollectionUtils.isNotEmpty(this.goodsForms)){
            if(goodsForms.get(0).getLabel().length() > 1000){
                message = "唛头信息不超过1000个字符";
            }
            if(goodsForms.get(0).getName().length() > 1000){
                message = "货物信息不超过1000个字符";
            }
        }
        // 发货/收货地址是必填项
//        if (CollectionUtil.isEmpty(this.deliveryAddress)) {
//            for (AddOrderAddressForm address : deliveryAddress) {
//                if(StringUtils.isEmpty(address.getAddress())){
//                    message = "发货地址不能为空";
//                    log.warn("发货地址不能为空");
//                }
//                if(StringUtils.isEmpty(address.getContacts())){
//                    log.warn("联系人不能为空");
//                    message = "联系人不能为空";
//                }
//            }
//            log.warn("发货地址信息不能为空");
//            message = "发货地址信息不能为空";
//        }
//        if (CollectionUtil.isEmpty(this.shippingAddress)) {
//            for (AddOrderAddressForm address : deliveryAddress) {
//                if(StringUtils.isEmpty(address.getPhone())){
//                    log.warn("电话号码不能为空");
//                    message = "电话号码不能为空";
//                }
//                if(StringUtils.isEmpty(address.getAddress())){
//                    log.warn("发货地址不能为空");
//                    message = "发货地址不能为空";
//                }
//                if(StringUtils.isEmpty(address.getContacts())){
//                    log.warn("联系人不能为空");
//                    message = "联系人不能为空";
//                }
//            }
//            log.warn("收货地址信息不能为空");
//            message = "收货地址信息不能为空";
//        }
//        if (this.notificationAddress.size() == 0 ||
//                StringUtils.isEmpty(this.notificationAddress.get(0).getAddress())) {
//            this.notificationAddress = null;
//        }
//
//        //货品信息
//        for (AddGoodsForm goodsForm : goodsForms) {
//            if (!goodsForm.checkCreateAirOrder()) {
//                message = "商品信息有缺失";
//            }
//        }
        if(this.cabinetType.equals(1)){
            if(CollectionUtil.isNotEmpty(this.cabinetSizeNumbers)){
                for (AddCabinetSizeNumber cabinetSizeNumber : this.cabinetSizeNumbers) {
                    if(StringUtils.isEmpty(cabinetSizeNumber.getCabinetTypeSize())){
                        log.warn("货柜大小类型信息不能为空");
                        message = "货柜大小类型信息不能为空";
                    }
                    if(cabinetSizeNumber.getNumber() == null ){
                        log.warn("柜子数量信息不能为空");
                        message = "柜子数量信息不能为空";
                    }
                }
            }

        }

        return message;
    }

    /**
     * 拼装地址
     */
    public void assemblyAddress() {
        this.orderAddressForms = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(this.deliveryAddress)){
            this.orderAddressForms.addAll(this.deliveryAddress);
        }
        if(CollectionUtil.isNotEmpty(this.shippingAddress)){
            this.orderAddressForms.addAll(this.shippingAddress);
        }

        if (CollectionUtil.isNotEmpty(this.notificationAddress)) {
            this.orderAddressForms.addAll(this.notificationAddress);
        }

        if (CollectionUtil.isNotEmpty(this.agentAddress)) {
            this.orderAddressForms.addAll(this.agentAddress);
        }
    }

    public void assemblyUP() {
        if(CollectionUtil.isNotEmpty(this.orderAddressForms)){
            for (AddOrderAddressForm orderAddressForm : this.orderAddressForms) {
                orderAddressForm.setAddress(orderAddressForm.getAddress().toUpperCase());
            }
        }
        if(CollectionUtil.isNotEmpty(this.goodsForms)){
            for (AddGoodsForm goodsForm : this.goodsForms) {
                if(goodsForm.getName() != null){
                    goodsForm.setName(goodsForm.getName().toUpperCase());
                }
                if(goodsForm.getLabel() != null){
                    goodsForm.setLabel(goodsForm.getLabel().toUpperCase());
                }
            }
        }
        if(this.so != null){
            this.so = this.so.toUpperCase();
        }
    }
}
