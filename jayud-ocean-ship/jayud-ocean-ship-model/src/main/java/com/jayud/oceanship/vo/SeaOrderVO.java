package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.oceanship.bo.AddSeaReplenishment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
public class SeaOrderVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "海运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long orderId;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单编号")
    private Long mainOrderId;

    @ApiModelProperty(value = "海运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态描述")
    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "贸易方式描述")
    private String termsDesc;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "起运港")
    private String portDeparture;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "目的港")
    private String portDestination;

    @ApiModelProperty(value = "中转港代码")
    private String transitPortCode;

    @ApiModelProperty(value = "中转港")
    private String transitPort;

    @ApiModelProperty(value = "操作人")
    private String orderTaker;

    @ApiModelProperty(value = "操作时间")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "货好时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime goodTime;

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

    @ApiModelProperty(value = "创建人(登录用户)")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订船信息")
    private SeaBookshipVO seaBookshipVO;

    @ApiModelProperty(value = "发货地址集合")
    private List<OrderAddressVO> deliveryAddress;

    @ApiModelProperty(value = "收货地址集合")
    private List<OrderAddressVO> shippingAddress;

    @ApiModelProperty(value = "通知地址集合")
    private List<OrderAddressVO> notificationAddress;

    @ApiModelProperty(value = "海运订单地址信息")
    private List<OrderAddressVO> orderAddressForms;

    @ApiModelProperty(value = "货品信息")
    private List<GoodsVO> goodsForms;

    @ApiModelProperty(value = "结算部门")
    private Long unitDepartmentId;

    @ApiModelProperty(value = "柜型大小")
    private Integer cabinetSize;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumberVO> cabinetSizeNumbers;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务员")
    private String bizName;

    @ApiModelProperty(value = "合同号")
    private String contractNo;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "所有附件信息")
    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "补料信息集合")
    private List<SeaReplenishmentVO> seaReplenishments;

    @ApiModelProperty(value = "截关时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closingTime;

    @ApiModelProperty(value = "截仓时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "代理人地址集合")
    private List<OrderAddressVO> agentAddress;

    @ApiModelProperty(value = "操作部门")
    private Long departmentId;

    @ApiModelProperty(value = "操作部门")
    private String department;

    @ApiModelProperty(value = "发货地")
    private String placeOfDelivery;

    public void processingAddress(OrderAddressVO addressVO) {
        switch (addressVO.getType()) {
            case 0:
                this.deliveryAddress = Collections.singletonList(addressVO);
                break;
            case 1:
                this.shippingAddress = Collections.singletonList(addressVO);
                break;
            case 2:
                this.notificationAddress = Collections.singletonList(addressVO);
                break;
            case 5:
                this.agentAddress = Collections.singletonList(addressVO);
                break;
        }
    }

    public void setImpAndExpType(Integer impAndExpType) {
        this.impAndExpType = impAndExpType;
        this.impAndExpTypeDesc = TradeTypeEnum.getDesc(impAndExpType);
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }
}
