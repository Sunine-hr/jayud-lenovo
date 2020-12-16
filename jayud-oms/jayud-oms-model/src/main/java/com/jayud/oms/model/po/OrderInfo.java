package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 主订单基础数据表
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderInfo对象", description="主订单基础数据表")
public class OrderInfo extends Model<OrderInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "客户code(customer_info code)")
    private String customerCode;

    @ApiModelProperty(value = "客户名(customer_info name")
    private String customerName;

    @ApiModelProperty(value = "业务员(system_user id)")
    private Integer bizUid;

    @ApiModelProperty(value = "业务员(system_user name)")
    private String bizUname;

    @ApiModelProperty(value = "结算单位(customer_info)")
    private String unitAccount;

    @ApiModelProperty(value = "结算代码(customer_info)")
    private String unitCode;

    @ApiModelProperty(value = "合同编号(contract_no)")
    private String contractNo;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "客户参考号")
    private String referenceNo;

    @ApiModelProperty(value = "对应业务类型")
    private String bizCode;

    @ApiModelProperty(value = "订单类型")
    private String classCode;

    @ApiModelProperty(value = "订单状态1正常 2草稿 3关闭")
    private Integer status;

    @ApiModelProperty(value = "业务所属部门")
    private String bizBelongDepart;

    @ApiModelProperty(value = "该订单选中得物流服务")
    private String selectedServer;

    @ApiModelProperty(value = "报关资料是否齐全")
    private String isDataAll;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "更新人")
    private String upUser;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "是否外部报关放行,针对没有出口报关的中港订单")
    private Boolean customsRelease;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
