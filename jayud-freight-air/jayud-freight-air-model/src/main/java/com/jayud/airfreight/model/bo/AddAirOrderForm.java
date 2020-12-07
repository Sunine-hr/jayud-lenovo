package com.jayud.airfreight.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.enums.OrderAddressEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 空运订单表
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class AddAirOrderForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "空运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单编号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "状态(k_0待接单,k_1空运接单,k_2订舱,k_3订单入仓, k_4确认提单,k_5确认离港,k_6确认到港,k_7海外代理k_8确认签收)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    private String settlementUnitCode;

    @ApiModelProperty(value = "接单法人id")
    private Long legalId;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:CIF,1:DUU,2:FOB,3:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "货好时间")
    private String goodTime;

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

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "空运订单地址信息")
    private List<AddOrderAddressForm> orderAddressForms;

    @ApiModelProperty(value = "货品信息")
    private List<AddGoodsForm> goodsForms;

    @ApiModelProperty(value = "创建人的类型(0:本系统,1:vivo)")
    private Integer createUserType;

    /**
     * 校验创建空运子订单参数
     */
    public boolean checkCreateOrder() {
        //空运
        if (this.legalId == null || StringUtils.isEmpty(this.settlementUnitCode)
                || this.impAndExpType == null || this.terms == null
                || StringUtils.isEmpty(this.portDepartureCode)
                || StringUtils.isEmpty(this.portDestinationCode)
                || this.goodTime == null) {
            return false;
        }
        //地址
        if (CollectionUtils.isEmpty(orderAddressForms)) {
            return false;
        }
        // 发货/收货地址是必填项
        int count = 0;
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            if (!orderAddressForm.checkCreateAirOrder()) return false;
            if (OrderAddressEnum.DELIVER_GOODS.getCode().equals(orderAddressForm.getType()) ||
                    OrderAddressEnum.RECEIVING_GOODS.getCode().equals(orderAddressForm.getType())) {
                ++count;
            }
        }
        if (count < 2) {
            return false;
        }
        //货品信息
        for (AddGoodsForm goodsForm : goodsForms) {
            if (!goodsForm.checkCreateAirOrder()) {
                return false;
            }
        }


        return true;
    }
}
