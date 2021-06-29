package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.OrderAddressEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SeaOrder对象", description = "海运订单表")
public class SeaOrderInfoVO extends Model<SeaOrderInfoVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "海运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "海运订单编号")
    private String subOrderNo;

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

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private String termsDesc;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "起运港代码")
    private String portDeparture;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestination;

    @ApiModelProperty(value = "中转港代码")
    private String transitPortCode;

    @ApiModelProperty(value = "中转港代码")
    private String transitPort;

    @ApiModelProperty(value = "柜型大小")
    private Integer cabinetSize;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "海外供应商id")
    private Long overseasSuppliersId;

    @ApiModelProperty(value = "代理服务类型（0:清关,1:配送）多个逗号隔开")
    private String proxyServiceType;

    @ApiModelProperty(value = "货好时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime goodTime;

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

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否需要录入费用(0:false,1:true)")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    @ApiModelProperty(value = "结算部门")
    private Long unitDepartmentId;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核状况 1为审核通过;2为审核不通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "订船信息")
    private SeaBookshipVO seaBookShip;

    @ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumberVO> cabinetSizeNumbers;

    @ApiModelProperty(value = "发货地址")
    private List<OrderAddressVO> deliveryAddress;

    @ApiModelProperty(value = "收货地址")
    private List<OrderAddressVO> shippingAddress;

    @ApiModelProperty(value = "通知地址")
    private List<OrderAddressVO> notificationAddress;

    @ApiModelProperty(value = "货品信息")
    private List<GoodsVO> goodsVOS;

    @ApiModelProperty(value = "货柜信息表")
    private List<SeaContainerInformationVO> seaContainerInfos;

    @ApiModelProperty(value = "补料信息集合")
    private List<SeaReplenishmentVO> seaReplenishments;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public SeaOrderInfoVO assembleAddr(List<OrderAddressVO> deliveryAddresses) {
        if (CollectionUtils.isEmpty(deliveryAddresses)) {
            return this;
        }
        deliveryAddress = new ArrayList<>();
        shippingAddress = new ArrayList<>();
        notificationAddress = new ArrayList<>();
        for (OrderAddressVO address : deliveryAddresses) {
            if (!this.subOrderNo.equals(address.getOrderNo())) {
                continue;
            }
            if (OrderAddressEnum.DELIVER_GOODS.getCode().equals(address.getType())) {
                deliveryAddress.add(address);
            }
            if (OrderAddressEnum.RECEIVING_GOODS.getCode().equals(address.getType())) {
                shippingAddress.add(address);
            }
            if (OrderAddressEnum.NOTICE.getCode().equals(address.getType())) {
                notificationAddress.add(address);
            }
        }
        return this;
    }

    public void assembleGoods(List<GoodsVO> goodsList) {
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }
        goodsVOS = new ArrayList<>();
        for (GoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())) {
                goodsVOS.add(goods);
            }
        }
    }
}
