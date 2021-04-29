package com.jayud.oms.model.vo.template.order;

import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.vo.InputOrderCustomsVO;
import com.jayud.oms.model.vo.InputSubOrderCustomsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderCustomsTemplate extends BaseOrderTemplate {

    @ApiModelProperty(value = "报关订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "进出口类型", required = true)
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸", required = true)
    private String portName;

    @ApiModelProperty(value = "订单状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "操作主体", required = true)
    private String legalName;

    @ApiModelProperty(value = "结算单位", required = true)
    private String unitName;

//    @ApiModelProperty(value = "报关文件", required = true)
//    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "报关单号", required = true)
    private String yunCustomsNo;

    @ApiModelProperty(value = "费用状态", required = true)
    private String costDesc;

    @ApiModelProperty(value = "费用状态")
    private Boolean cost;


//    @ApiModelProperty(value = "进出口类型")
//    private Integer goodsType;

    @ApiModelProperty(value = "报关抬头")
    private String title;

    @ApiModelProperty(value = "单双抬头")
    private String isTitle;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

//    @ApiModelProperty(value = "附件")
//    private List<FileView> fileViews = new ArrayList<>();

//    @ApiModelProperty(value = "接单人")
//    private String jiedanUser;
//
//    @ApiModelProperty(value = "接单时间")
//    private String jiedanTimeStr;


//    @ApiModelProperty(value = "通关口岸code")
//    private String portCode;


//    @ApiModelProperty(value = "六联单号")
//    private String encode;
//
//    @ApiModelProperty(value = "六联单号附件数组集合")
//    private List<FileView> encodePics = new ArrayList<>();


//    @ApiModelProperty(value = "提运单")
//    private String airTransportNo;
//
//    @ApiModelProperty(value = "提运单附件数组集合")
//    private List<FileView> airTransportPics = new ArrayList<>();
//
//    @ApiModelProperty(value = "提运单号")
//    private String seaTransportNo;
//
//    @ApiModelProperty(value = "提运单号附件数组集合")
//    private List<FileView> seaTransportPics = new ArrayList<>();

//    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
//    private String isAgencyTax;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

//    @ApiModelProperty(value = "报关单数")
//    private String number;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;


    public void setGoodsType(Integer goodsType) {
//        this.goodsType = goodsType;
        this.goodsTypeDesc = TradeTypeEnum.getDesc(goodsType);
    }


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void assemblyData(InputOrderCustomsVO orderCustomsForm) {
        this.setGoodsType(orderCustomsForm.getGoodsType());
        this.setPortName(orderCustomsForm.getPortName());
        this.setLegalEntityId(orderCustomsForm.getLegalEntityId());
        this.setLegalName(orderCustomsForm.getLegalName());
    }

    public void setCost(Boolean cost) {
        this.cost = cost;
        if (cost){
            this.costDesc="是";
        }else {
            this.costDesc="否";
        }
    }

}
