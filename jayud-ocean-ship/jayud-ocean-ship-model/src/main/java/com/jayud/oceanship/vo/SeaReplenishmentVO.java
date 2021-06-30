package com.jayud.oceanship.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
@Accessors(chain = true)
@ApiModel(value="SeaReplenishment对象", description="海运补料表")
public class SeaReplenishmentVO extends Model<SeaReplenishmentVO> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "截补料单号")
    private String orderNo;

    @ApiModelProperty(value = "海运订单id")
    private Long seaOrderId;

    @ApiModelProperty(value = "海运订单id")
    private String seaOrderNo;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

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

    @ApiModelProperty(value = "是否已提单")
    private Integer isBillOfLading;

    @ApiModelProperty(value = "是否已放单")
    private Integer isReleaseOrder;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "贸易方式")
    private String termsDesc;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "起运港")
    private String portDepartureName;

    @ApiModelProperty(value = "目的港")
    private String portDestinationName;

    @ApiModelProperty(value = "中转港")
    private String transitPortCode;

    @ApiModelProperty(value = "中转港")
    private String transitPort;

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

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型类型名字")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumberVO> cabinetSizeNumbers;

    @ApiModelProperty(value = "货柜信息集合")
    private List<SeaContainerInformationVO> seaContainerInformations;

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

    @ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

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
    private String additionalService;

    @ApiModelProperty(value = "附加服务")
    private List<String> additionalServices;

    @ApiModelProperty(value = "开船时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailingTime;

    @ApiModelProperty(value = "订柜信息")
    private String orderingInformation;

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "提单重量")
    private Double billLadingWeight;

    public void getFile(String path){
        this.fileViewList = com.jayud.common.utils.StringUtils.getFileViews(this.getFilePath(),this.getFileName(),path);
    }

    public void assemblyAdditionalServices(){
        if(this.additionalService != null){
            this.additionalServices = Arrays.asList(this.additionalService.split(";"));
        }

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

    public void assemblyCabinetInfo(List<CabinetSizeNumberVO> cabinetSizeNumberVOS) {
        StringBuilder sb = new StringBuilder();
        for (CabinetSizeNumberVO cabinetSizeNumberVO : cabinetSizeNumberVOS) {
            sb.append(cabinetSizeNumberVO.getCabinetTypeSize()).append("/").append(cabinetSizeNumberVO.getNumber()).append(" ");
        }
        this.orderingInformation = sb.toString();
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
