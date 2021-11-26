package com.jayud.Inlandtransport.model.bo;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 内陆订单
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@Data
public class InputOrderInlandTransportForm extends Model<InputOrderInlandTransportForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内陆订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "流程状态 前端不用管")
    private Integer processStatus;

    @ApiModelProperty(value = "状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核, NL_3_1派车审核不通过,NL_3_2派车审核驳回,L_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)")
    private String status;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T 5t 8T 10T 12T 20GP 40GP 45GP..)")
    private String vehicleSize;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "结算单位名称")
    private String unitName;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "法人主体ID")
    private Long legalEntityId;

//    @ApiModelProperty(value = "接单人(登录用户名)")
//    private String orderTaker;

//    @ApiModelProperty(value = "接单日期")
//    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "创建人(登录用户) 前端不用管")
    private String createUser;

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;

    @ApiModelProperty("提货地址")
    private List<OrderDeliveryAddress> pickUpAddressList;

    @ApiModelProperty("送货地址")
    private List<OrderDeliveryAddress> orderDeliveryAddressList;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "(0:本系统,2:scm)")
    private Integer createUserType;

    @ApiModelProperty(value = "指令")
    private String cmd;

    @ApiModelProperty(value = "外部调用标识(1类型1  2类型2)")
    private Integer type;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void checkCreateOrder() {
        String msg = "不能为空";
        if (StringUtils.isEmpty(this.vehicleSize)) {
            throw new JayudBizException("车型尺寸" + msg);
        }
        if (StringUtils.isEmpty(this.unitCode)) {
            throw new JayudBizException("结算单位code" + msg);
        }
        if (vehicleType == null) {
            throw new JayudBizException("车型" + msg);
        }
        if (StringUtils.isEmpty(this.legalName)) {
            throw new JayudBizException("操作主体" + msg);
        }
        if (this.legalEntityId == null) {
            throw new JayudBizException("操作主体id" + msg);
        }

    }


}
