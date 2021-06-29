package com.jayud.oceanship.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

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
@ApiModel(value = "SeaBill对象", description = "海运提单表")
public class SeaBillFormVO extends Model<SeaBillFormVO> {

    private static final long serialVersionUID = 1L;

    //@ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "提单单号")
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

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "货好时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime goodTime;

    @ApiModelProperty(value = "运费是否到付")
    private Boolean isFreightCollect;

    @ApiModelProperty(value = "其他费用是否到付")
    private Boolean isOtherExpensesPaid;

    @ApiModelProperty(value = "是否危险品")
    private Boolean isDangerousGoods;

    @ApiModelProperty(value = "是否带电")
    private Boolean isCharged;

    @ApiModelProperty(value = "中转港")
    private String transitPortCode;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型类型名字")
    private String cabinetTypeName;

    @ApiModelProperty(value = "运输方式")
    private String transportClause;

    @ApiModelProperty(value = "船名字")
    private String shipName;

    @ApiModelProperty(value = "船次")
    private String shipNumber;

    @ApiModelProperty(value = "出单方式")
    private String deliveryMode;

    @ApiModelProperty(value = "附加服务")
    private String additionalService;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "收货地")
    private String placeOfDelivery;

    @ApiModelProperty(value = "截关时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closingTime;

    @ApiModelProperty(value = "截仓时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "开船时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailingTime;

    @ApiModelProperty(value = "订柜信息")
    private String orderingInformation;

    @ApiModelProperty(value = "发货人信息")
    private String shipperInformation;

    @ApiModelProperty(value = "收货人信息")
    private String consigneeInformation;

    @ApiModelProperty(value = "通知人信息")
    private String notifierInformation;

    @ApiModelProperty(value = "代理人信息")
    private String agentInformation;

    @ApiModelProperty(value = "唛头")
    private String shippingMark;

    @ApiModelProperty(value = "货物名称")
    private String goodName;

    @ApiModelProperty(value = "板数")
    private Integer boardNumber;

    @ApiModelProperty(value = "板数单位")
    private String plateUnit;

    @ApiModelProperty(value = "件数")
    private Integer number;

    @ApiModelProperty(value = "件数单位")
    private String numberUnit;

    @ApiModelProperty(value = "总重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "提单号")
    private String billNo;

    @ApiModelProperty(value = "交仓码头")
    private String deliveryWharf;

    @ApiModelProperty(value = "航程")
    private Integer voyage;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式")
    private String termsDesc;

    @ApiModelProperty(value = "起运港")
    private String portDepartureName;

    @ApiModelProperty(value = "目的港")
    private String portDestinationName;

    @ApiModelProperty(value = "中转港")
    private String transitPort;

    @ApiModelProperty(value = "柜量名字")
    private String cabinetSizeName;

    //@ApiModelProperty(value = "货柜信息集合")
    private List<SeaContainerInformationVO> seaContainerInformations;

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

    @ApiModelProperty(value = "附加服务")
    private List<String> additionalServices;

    @ApiModelProperty(value = "提单类型")
    private Integer type;

    @ApiModelProperty(value = "是否拼柜")
    private Boolean isSpell;

    @ApiModelProperty(value = "拼柜订单号集合")
    private String spellOrderNo;

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
