package com.jayud.tms.model.po;

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
 * 订单派车信息
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderSendCars对象", description="订单派车信息")
public class OrderSendCars extends Model<OrderSendCars> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运输订单号(order_transport orderno)")
    private String transportNo;

    @ApiModelProperty(value = "运输对应子订单(订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "六联单号文件地址")
    private String encodeUrl;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private Integer vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片")
    private String cntrPic;

    @ApiModelProperty(value = "供应商code(supplier_info code)")
    private String supplierCode;

    @ApiModelProperty(value = "供应商中文名(supplier_info supplier_ch_name)")
    private String supplierName;

    @ApiModelProperty(value = "大陆车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "大陆司机名")
    private String driverName;

    @ApiModelProperty(value = "HK车牌号")
    private String hkLicensePlate;

    @ApiModelProperty(value = "HK司机名")
    private String hkDriverName;

    @ApiModelProperty(value = "仓库代码(warehouse_info)")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称(warehouse_info)")
    private String warehouseName;

    @ApiModelProperty(value = "要求")
    private String remarks;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态(0-草稿 1-审核中 2-审核驳回 3-审核通过)")
    private Integer status;

    @ApiModelProperty(value = "通过/驳回原因")
    private String optDes;

    @ApiModelProperty(value = "通过/驳回人")
    private Integer optUid;

    @ApiModelProperty(value = "通过/驳回人名字")
    private String optUname;

    @ApiModelProperty(value = "通过/驳回时间")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人id")
    private Integer createUid;

    @ApiModelProperty(value = "创建人姓名")
    private String createUname;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
