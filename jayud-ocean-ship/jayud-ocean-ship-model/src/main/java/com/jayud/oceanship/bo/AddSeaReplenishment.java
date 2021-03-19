package com.jayud.oceanship.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
    private LocalDateTime cutReplenishTime;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否已提单")
    private Integer isBillOfLading;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "货好时间")
    private LocalDateTime goodTime;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
