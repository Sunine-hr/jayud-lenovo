package com.jayud.oceanship.bo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import com.jayud.oceanship.po.CabinetSizeNumber;
import com.jayud.oceanship.po.SeaContainerInformation;
import com.jayud.oceanship.vo.GoodsVO;
import com.jayud.oceanship.vo.OrderAddressVO;
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
public class AddSeaReplenishment extends Model<AddSeaReplenishment> {

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
    private String cutReplenishTime;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "是否已提单")
    private Integer isBillOfLading;

    @ApiModelProperty(value = "是否已放单")
    private Integer isReleaseOrder;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

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

    @ApiModelProperty(value = "中转港")
    private String transitPortCode;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型类型名字")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumber> cabinetSizeNumbers;

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

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "提单重量")
    private Double billLadingWeight;

    @ApiModelProperty(value = "货柜信息集合")
    private List<SeaContainerInformation> seaContainerInformations;

    @ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "截关时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String closingTime;

    @ApiModelProperty(value = "截仓时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String cutOffTime;

    @ApiModelProperty(value = "代理人地址集合")
    private List<AddOrderAddressForm> agentAddress;

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

    @ApiModelProperty(value = "开船时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String sailingTime;

    @ApiModelProperty(value = "订柜信息")
    private String orderingInformation;


    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();


    public void getFile(String path){
        this.fileViewList = com.jayud.common.utils.StringUtils.getFileViews(this.getFilePath(),this.getFileName(),path);
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

    public void toUp(){
        if(this.shipName != null){
            this.shipName = this.shipName.toUpperCase();
        }
        if(this.shipNumber != null){
            this.shipNumber = this.shipNumber.toUpperCase();
        }
        if(this.so != null){
            this.so = this.so.toUpperCase();
        }
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
        if(this.subNo != null){
            this.subNo = subNo.toUpperCase();
        }
        if(this.mainNo != null){
            this.mainNo = mainNo.toUpperCase();
        }

    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
