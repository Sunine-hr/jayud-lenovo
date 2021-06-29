package com.jayud.oceanship.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javassist.expr.NewArray;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 海运补料表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
@Accessors(chain = true)
@ApiModel(value = "SeaReplenishment对象", description = "海运补料表")
public class SeaReplenishmentFormVO extends Model<SeaReplenishmentFormVO> {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "截补料单号")
    private String orderNo;

    //@ApiModelProperty(value = "海运订单id")
    private Long orderId;

    //@ApiModelProperty(value = "海运订单id")
    private Long seaOrderId;

    @ApiModelProperty(value = "海运订单号")
    private String seaOrderNo;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    //@ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    //@ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    //@ApiModelProperty(value = "是否已提单")
    private Integer isBillOfLading;

    //@ApiModelProperty(value = "是否已放单")
    private Integer isReleaseOrder;

    //@ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    //@ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "贸易方式")
    private String termsDesc;

    //@ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    //@ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "起运港")
    private String portDepartureName;

    @ApiModelProperty(value = "目的港")
    private String portDestinationName;

    //@ApiModelProperty(value = "中转港")
    private String transitPortCode;

    @ApiModelProperty(value = "中转港")
    private String transitPort;

    @ApiModelProperty(value = "货好时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime goodTime;

    //@ApiModelProperty(value = "运费是否到付(1代表true,0代表false)")
    private Boolean isFreightCollect;

   // @ApiModelProperty(value = "其他费用是否到付(1代表true,0代表false)")
    private Boolean isOtherExpensesPaid;

    //@ApiModelProperty(value = "是否危险品(1代表true,0代表false)")
    private Boolean isDangerousGoods;

   // @ApiModelProperty(value = "是否带电(1代表true,0代表false)")
    private Boolean isCharged;

    //@ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型名字")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜量名字")
    private String cabinetSizeName;

    //@ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumberVO> cabinetSizeNumbers;

    //@ApiModelProperty(value = "货柜信息集合")
    private List<SeaContainerInformationVO> seaContainerInformations;

    //@ApiModelProperty(value = "发货地址集合")
    private List<OrderAddressVO> deliveryAddress;

   // @ApiModelProperty(value = "收货地址集合")
    private List<OrderAddressVO> shippingAddress;

   // @ApiModelProperty(value = "通知地址集合")
    private List<OrderAddressVO> notificationAddress;

    //@ApiModelProperty(value = "海运订单地址信息")
    private List<OrderAddressVO> orderAddressForms;

    //@ApiModelProperty(value = "货品信息")
    private List<GoodsVO> goodsVOS;

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "提单重量")
    private Double billLadingWeight;

    //@ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态描述")
    private String processStatusDesc;

    //@ApiModelProperty(value = "客户名称")
    private String customerName;

    //@ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    //@ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    //@ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    //@ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    //@ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    //@ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    //@ApiModelProperty(value = "状态")
    private String status;

    //@ApiModelProperty(value = "订单流程状态")
    private String statusDesc;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "截关时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closingTime;

    @ApiModelProperty(value = "截仓时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "代理人地址集合")
    private List<OrderAddressVO> agentAddress;

    @ApiModelProperty(value = "发货地")
    private String placeOfDelivery;

    @ApiModelProperty(value = "运输条款")
    private String transportClause;

    @ApiModelProperty(value = "船名")
    private String shipName;

    @ApiModelProperty(value = "船次")
    private String shipNumber;

    @ApiModelProperty(value = "出单方式")
    private String deliveryMode;

    @ApiModelProperty(value = "附加服务")
    private List<String> additionalServices;

    @ApiModelProperty(value = "附加服务")
    private String additionalService;

    @ApiModelProperty(value = "开船时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailingTime;

    @ApiModelProperty(value = "订柜信息")
    private String orderingInformation;

    public void getFile(String path){
        this.fileViewList = StringUtils.getFileViews(this.getFilePath(),this.getFileName(),path);
    }

    /**
     * @param mainOrderObjs 远程客户对象集合
     */
    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                this.mainOrderId = json.getStr("id");
                this.customerCode = json.getStr("customerCode");
                break;
            }
        }

    }

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();

        for (GoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())
                    && BusinessTypeEnum.HY.getCode().equals(goods.getBusinessType())) {
                sb.append(goods.getName())
                        .append(" ").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount()).append(goods.getPlateUnit())
                        .append(",").append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
                        .append(",").append("重量:").append(goods.getTotalWeight()).append("KG")
                        .append(";");
            }
        }

        this.goodsInfo = sb.toString();
    }


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

    public void assemblyCabinetInfo(List<CabinetSizeNumberVO> cabinetSizeNumberVOS) {
        StringBuilder sb = new StringBuilder();
        for (CabinetSizeNumberVO cabinetSizeNumberVO : cabinetSizeNumberVOS) {
            sb.append(cabinetSizeNumberVO.getCabinetTypeSize()).append("/").append(cabinetSizeNumberVO.getNumber()).append(" ");
        }
        this.cabinetSizeName = sb.toString();
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void assemblyAdditionalServices(){
        this.additionalServices = Arrays.asList(additionalService.split(";"));
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
