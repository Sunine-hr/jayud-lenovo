package com.jayud.oceanship.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.utils.FileView;
import com.jayud.oceanship.po.CabinetSizeNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海运订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@Slf4j
public class AddSeaOrderForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "海运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long orderId;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "海运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "中转港代码")
    private String transitPortCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "柜型大小")
    private Integer cabinetSize;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumber> cabinetSizeNumbers;

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

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

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

    @ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();


    /**
     * 校验创建海运子订单参数
     */
    public boolean checkCreateOrder() {
        //海运
        if (this.legalEntityId == null || StringUtils.isEmpty(this.unitCode)
                || this.impAndExpType == null || this.terms == null
                || StringUtils.isEmpty(this.portDepartureCode)
                || StringUtils.isEmpty(this.portDestinationCode)
                || this.goodTime == null) {
            return false;
        }
        // 发货/收货地址是必填项
        if (CollectionUtils.isEmpty(this.deliveryAddress)) {
            log.warn("发货地址信息不能为空");
            return false;
        }
        if (CollectionUtils.isEmpty(this.shippingAddress)) {
            log.warn("收货地址信息不能为空");
            return false;
        }
        if (this.notificationAddress.size() == 0 ||
                StringUtils.isEmpty(this.notificationAddress.get(0).getAddress())) {
            this.notificationAddress = null;
        }

        //货品信息
        for (AddGoodsForm goodsForm : goodsForms) {
            if (!goodsForm.checkCreateAirOrder()) {
                return false;
            }
        }

        return true;
    }

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
}
