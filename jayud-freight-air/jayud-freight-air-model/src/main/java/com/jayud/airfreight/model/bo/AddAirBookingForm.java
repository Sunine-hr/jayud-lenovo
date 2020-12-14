package com.jayud.airfreight.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 空运订舱
 * </p>
 *
 * @author LDR
 * @since 2020-12-03
 */
@Data
@Slf4j
public class AddAirBookingForm extends Model<AddAirBookingForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键 编辑需要传空运订舱id")
    private Long id;

    @ApiModelProperty(value = "空运订单编号")
    private String airOrderNo;

    @ApiModelProperty(value = "空运订单id")
    private Long airOrderId;

    @ApiModelProperty(value = "代理供应商id")
    private Long agentSupplierId;

    @ApiModelProperty(value = "入仓号")
    private String warehousingNo;

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "截关日期")
    private String cutOffDate;

    @ApiModelProperty(value = "航空公司")
    private String airlineCompany;

    @ApiModelProperty(value = "航班")
    private String flight;

    @ApiModelProperty(value = "ETD 预计离港时间")
    private String etd;

    @ApiModelProperty(value = "ETA 预计到港时间")
    private String eta;

    @ApiModelProperty(value = "ATD 实际离岗时间")
    private String atd;

    @ApiModelProperty(value = "ATA 实际到港时间")
    private String ata;

    @ApiModelProperty(value = "交仓地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "交仓仓库")
    private String deliveryWarehouse;

//    @ApiModelProperty(value = "附件集合")
//    private List<FileView> fileViewList = new ArrayList<>();

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public boolean checkBookingSpaceOptParam() {
        String title = "订舱操作";
        if (StringUtils.isEmpty(this.airOrderNo)) {
            log.warn(title + " 空运订单编号必填");
            return false;
        }
        if (this.airOrderId == null) {
            log.warn(title + " 空运订单id必填");
            return false;
        }
        if (this.agentSupplierId == null) {
            log.warn(title + " 供应商id必填");
            return false;
        }
        if (StringUtils.isEmpty(this.warehousingNo)) {
            log.warn(title + " 入仓号必填");
            return false;
        }
        if (StringUtils.isEmpty(this.deliveryAddress)) {
            log.warn(title + " 交仓地址必填");
            return false;
        }
        if (StringUtils.isEmpty(this.deliveryWarehouse)) {
            log.warn(title + " 交仓仓库必填");
            return false;
        }
        return true;
    }

    public boolean checkConfirmLadingBillOptParam() {
        String title = "确认提单操作";
        if (this.agentSupplierId == null) {
            log.warn(title + " 供应商id必填");
            return false;
        }
        if (StringUtils.isEmpty(this.airlineCompany)) {
            log.warn(title + " 航空公司必填");
            return false;
        }
        if (StringUtils.isEmpty(this.flight)) {
            log.warn(title + " 航班必填");
            return false;
        }
        if (StringUtils.isEmpty(this.mainNo)) {
            log.warn(title + " 主单号必填");
            return false;
        }
        if (StringUtils.isEmpty(this.etd)) {
            log.warn(title + " 预计离港时间必填");
            return false;
        }
        if (StringUtils.isEmpty(this.eta)) {
            log.warn(title + " 预计到港时间必填");
            return false;
        }
        return true;
    }

    public boolean checkConfirmATDOptParam() {
        String title = "确认离港操作";
        if (this.id == null) {
            log.warn(title + " 订舱id必填");
            return false;
        }
        if (StringUtils.isEmpty(this.atd)) {
            log.warn(title + " 实际离港时间必填");
            return false;
        }
        return true;
    }

    public boolean checkConfirmATAOptParam() {
        String title = "确认到港操作";
        if (this.id == null) {
            log.warn(title + " 订舱id必填");
            return false;
        }
        if (StringUtils.isEmpty(this.ata)) {
            log.warn(title + " 实际到港时间必填");
            return false;
        }
        return true;
    }
}
