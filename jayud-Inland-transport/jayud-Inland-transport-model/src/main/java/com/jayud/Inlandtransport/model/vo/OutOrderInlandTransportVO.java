package com.jayud.Inlandtransport.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.Inlandtransport.model.bo.InputOrderOutTakeAdrForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回内陆部分信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderInlandTransport对象", description = "内陆订单")
public class OutOrderInlandTransportVO  extends Model<OutOrderInlandTransportVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内陆订单主键ID")
    private Long id;

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "主订单编号(order_no)")
    private String mainOrderNo;

    @ApiModelProperty(value = "内陆订单编号")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;


    @ApiModelProperty(value = "提货地址")
    private List<InputOrderOutTakeAdrForm> takeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "送货地址")
    private List<InputOrderOutTakeAdrForm> takeAdrForms2 = new ArrayList<>();

    @ApiModelProperty(value = "外部调用标识(1类型1  2类型2)")
    private Integer type;


}
