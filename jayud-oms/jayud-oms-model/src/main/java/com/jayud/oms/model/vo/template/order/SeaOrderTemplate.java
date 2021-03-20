package com.jayud.oms.model.vo.template.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.bo.InputSeaOrderForm;
import com.jayud.oms.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

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
public class SeaOrderTemplate {

    @ApiModelProperty(value = "海运订单主键")
    private Long orderId;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "货好时间", required = true)
    private String goodTime;

    @ApiModelProperty(value = "进出口类型", required = true)
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式", required = true)
    private String termsDesc;

    @ApiModelProperty(value = "柜型", required = true)
    private String cabinetTypeName;

    @ApiModelProperty(value = "订单状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "操作主体",required = true)
    private String legalName;

    @ApiModelProperty(value = "结算单位",required = true)
    private String unitName;

    @ApiModelProperty(value = "货物信息",required = true)
    private String goodsInfo;

    @ApiModelProperty(value = "始发港",required = true)
    private String portDeparture;

    @ApiModelProperty(value = "目的港",required = true)
    private String portDestination;

    @ApiModelProperty(value = "船次",required = true)
    private String shipNumber;

    @ApiModelProperty(value = "开船时间",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime etd;

    @ApiModelProperty(value = "到港时间",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eta;

    @ApiModelProperty(value = "提单号",required = true)
    private String mainNo;


    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

//    @ApiModelProperty(value = "流程状态描述")
//    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;


    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;


//    @ApiModelProperty(value = "起运港代码")
//    private String portDepartureCode;

//    @ApiModelProperty(value = "目的港代码")
//    private String portDestinationCode;


//    @ApiModelProperty(value = "中转港代码")
//    private String transitPortCode;
//
//    @ApiModelProperty(value = "中转港")
//    private String transitPort;

//    @ApiModelProperty(value = "操作人")
//    private String orderTaker;
//
//    @ApiModelProperty(value = "操作时间")
//    private String receivingOrdersDate;


    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createTime;

//    @ApiModelProperty(value = "发货地址集合")
//    private List<InputOrderAddressVO> deliveryAddress;
//
//    @ApiModelProperty(value = "收货地址集合")
//    private List<InputOrderAddressVO> shippingAddress;
//
//    @ApiModelProperty(value = "通知地址集合")
//    private List<InputOrderAddressVO> notificationAddress;
//
//    @ApiModelProperty(value = "海运订单地址信息")
//    private List<InputOrderAddressVO> orderAddressForms;

    @ApiModelProperty(value = "货品信息")
    @JsonIgnore
    private List<InputGoodsVO> goodsForms;

//    @ApiModelProperty(value = "结算部门")
//    private Long unitDepartmentId;
//
//    @ApiModelProperty(value = "柜号")
//    private String cabinetNumber;
//
//    @ApiModelProperty(value = "封条")
//    private String paperStripSeal;
//
//    @ApiModelProperty(value = "柜型大小")
//    private Integer cabinetSize;
//
//    @ApiModelProperty(value = "柜型类型")
//    private Integer cabinetType;
//
//    @ApiModelProperty(value = "柜型大小")
//    private String cabinetSizeName;
//
//
//    @ApiModelProperty(value = "截补料时间")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    private String cutReplenishTime;
//
//    @ApiModelProperty(value = "所有附件信息")
//    private List<FileView> allPics = new ArrayList<>();
//
//    @ApiModelProperty(value = "供应商名称")
//    private String supplierName;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单id")
    private Long mainOrderId;

    @ApiModelProperty(value = "状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)")
    private String status;



    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setGoodsForms(List<InputGoodsVO> goodsForms) {
        StringBuilder sb = new StringBuilder();
        for (InputGoodsVO goodsForm : goodsForms) {
            sb.append(goodsForm.getName())
                    .append("/").append(goodsForm.getPlateAmount() == null ? 0 : goodsForm.getPlateAmount()).append(goodsForm.getPlateUnit())
                    .append("/").append(goodsForm.getBulkCargoAmount()).append(goodsForm.getBulkCargoUnit())
                    .append("/").append("重量").append(goodsForm.getTotalWeight()).append("KG")
                    .append(",");
        }
        this.goodsInfo = sb.toString();
    }

    public void assemblyData(InputSeaOrderVO seaOrderForm) {
        InputSeaBookshipVO seaBookshipVO = seaOrderForm.getSeaBookshipVO();
        if (seaBookshipVO != null) {
            this.shipNumber = seaBookshipVO.getShipNumber();
            this.etd = seaBookshipVO.getEtd();
            this.eta = seaBookshipVO.getEta();
            this.mainNo = seaBookshipVO.getMainNo();
        }

    }
}
