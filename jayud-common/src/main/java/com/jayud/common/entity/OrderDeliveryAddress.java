package com.jayud.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单地址(提货/送货地址)
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class OrderDeliveryAddress {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单地址主键")
    private Long orderAddressId;

    @ApiModelProperty(value = "商品主键")
    private Long goodsId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "交货日期(提货日期/送货日期)")
    private String deliveryDate;

    @ApiModelProperty(value = "入仓号,送货有")
    private String enterWarehouseNo;

    @ApiModelProperty(value = "地址类型(3:提货,4:送货)")
    private Integer addressType;

    @ApiModelProperty(value = "货品描述")
    private String goodsName;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "板数单位")
    private String plateUnit;

    @ApiModelProperty(value = "散货件数")
    private Integer bulkCargoAmount;

    @ApiModelProperty(value = "散货单位")
    private String bulkCargoUnit;

    @ApiModelProperty(value = "尺寸(长宽高)")
    private String size;

    @ApiModelProperty(value = "总重量(kg)")
    private Double totalWeight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "业务类型 前端不用管 ")
    private Integer businessType;

    @ApiModelProperty(value = "业务主键 前端不用管")
    private Long businessId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    public void checkCreateOrder() {
        String msg = "不能为空";
        if (StringUtils.isEmpty(this.goodsName)) {
            throw new JayudBizException("联系人" + msg);
        }
        if (StringUtils.isEmpty(this.phone)) {
            throw new JayudBizException("联系电话" + msg);
        }
        if (StringUtils.isEmpty(this.address)) {
            throw new JayudBizException("提货/送货地址" + msg);
        }
        if (this.deliveryDate == null) {
            throw new JayudBizException("提货/送货日期" + msg);
        }
    }

}
