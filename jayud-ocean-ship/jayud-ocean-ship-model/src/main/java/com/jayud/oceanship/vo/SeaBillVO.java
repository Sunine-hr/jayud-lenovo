package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.oceanship.po.SeaContainerInformation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 提单信息
 * </p>
 *
 * @author LLJ
 * @since 2021-06-23
 */
@Data
public class SeaBillVO extends Model<SeaBillVO> {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提单订单号")
    private String orderNo;

    @ApiModelProperty(value = "海运订单id")
    private Long seaOrderId;

    @ApiModelProperty(value = "海运订单号")
    private String seaOrderNo;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

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

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

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

    @ApiModelProperty(value = "提单类型")
    private Integer type;

    @ApiModelProperty(value = "是否拼柜(1代表true,0代表false)")
    private Boolean isSpell;

    @ApiModelProperty(value = "拼柜订单号集合")
    private String spellOrderNo;

    @ApiModelProperty(value = "货柜信息集合")
    private List<SeaContainerInformationVO> seaContainerInformations;

}
