package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderInfoForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "客户ID(customer id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer customerId;

    @ApiModelProperty(value = "报价id(offer_info id)", position = 4)
    @JSONField(ordinal = 4)
    @NotNull(message = "报价id不能为空")
    private Integer offerInfoId;

    @ApiModelProperty(value = "订柜尺寸[海运费](template_cope_receivable specification_code -> quotation_type code)", position = 4)
    @JSONField(ordinal = 4)
    private String reserveSize;

    @ApiModelProperty(value = "集货仓库代码[内陆费](template_cope_receivable specification_code ->shipping_area warehouse_code)", position = 5)
    @JSONField(ordinal = 5)
    private String storeGoodsWarehouseCode;

    @ApiModelProperty(value = "集货仓库名称(shipping_area warehouse_name)", position = 6)
    @JSONField(ordinal = 6)
    private String storeGoodsWarehouseName;

    @ApiModelProperty(value = "目的仓库代码(fab_warehouse warehouse_code)", position = 7)
    @JSONField(ordinal = 7)
    private String destinationWarehouseCode;

    @ApiModelProperty(value = "目的仓库名称(fab_warehouse warehouse_name)", position = 8)
    @JSONField(ordinal = 8)
    private String destinationWarehouseName;

    @ApiModelProperty(value = "是否上门提货(0否 1是,order_pick)", position = 9)
    @JSONField(ordinal = 9)
    private Integer isPick;

    @ApiModelProperty(value = "创建日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人ID(customer id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer createUserId;

    @ApiModelProperty(value = "创建人名称(customer user_name)", position = 14)
    @JSONField(ordinal = 14)
    private String createUserName;

    @ApiModelProperty(value = "订单来源，默认为1，web端；", position = 15)
    @JSONField(ordinal = 15)
    private String orderOrigin;

    @ApiModelProperty(value = "提单号，根据配载单关联规则生成", position = 16)
    @JSONField(ordinal = 16)
    private Integer bolNo;

    @ApiModelProperty(value = "是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)", position = 17)
    @JSONField(ordinal = 17)
    private Integer needDeclare;

    @ApiModelProperty(value = "是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)", position = 18)
    @JSONField(ordinal = 18)
    private Integer needClearance;

    @ApiModelProperty(value = "备注", position = 19)
    @JSONField(ordinal = 19)
    private String remark;

    //订单补充计费重信息
    @ApiModelProperty(value = "收费重(KG)", position = 20)
    @JSONField(ordinal = 20)
    private BigDecimal chargeWeight;

    @ApiModelProperty(value = "材积重(KG)", position = 21)
    @JSONField(ordinal = 21)
    private BigDecimal volumeWeight;

    @ApiModelProperty(value = "实际重量(KG)", position = 22)
    @JSONField(ordinal = 22)
    private BigDecimal actualWeight;

    @ApiModelProperty(value = "实际体积(m3)", position = 23)
    @JSONField(ordinal = 23)
    private BigDecimal actualVolume;

    @ApiModelProperty(value = "总箱数", position = 24)
    @JSONField(ordinal = 24)
    private Integer totalCartons;

    @ApiModelProperty(value = "Amazon Reference ID(亚马逊引用ID)", position = 24)
    @JSONField(ordinal = 24)
    private String amazonReferenceId;

    @ApiModelProperty(value = "扩展单号", position = 24)
    @JSONField(ordinal = 24)
    private String extensionNumber;

    @ApiModelProperty(value = "销售提成")
    private BigDecimal salesCommission;

    @ApiModelProperty(value = "前端状态代码\n" +
            "    草稿:0\n" +
            "    补资料:9\n" +
            "    已下单:10\n" +
            "    已收货:20\n" +
            "    转运中:30\n" +
            "    已签收:40\n" +
            "    已完成:50\n" +
            "    已取消:-1")
    private String frontStatusCode;

    @ApiModelProperty(value = "前端状态名称")
    private String frontStatusName;

    @ApiModelProperty(value = "后端状态代码\n" +
            "    草稿:0\n" +
            "    补资料:9\n" +
            "    已下单:10\n" +
            "        -- 内部小状态，这个不是流程状态\n" +
            "        -- 已审单\n" +
            "        -- 未审单\n" +
            "    已收货:20\n" +
            "    订单确认:30\n" +
            "    已签收:40\n" +
            "    已完成:50\n" +
            "    已取消:-1")
    private String afterStatusCode;

    @ApiModelProperty(value = "后端状态名称")
    private String afterStatusName;

    @ApiModelProperty(value = "结算方式(1票结 2月结)")
    private Integer clearingWay;

    /*订单对应箱号信息:order_case*/
    @ApiModelProperty(value = "订单对应箱号信息list", position = 25)
    @JSONField(ordinal = 25)
    private List<OrderCaseVO> orderCaseVOList;

    /*订单对应商品：order_shop*/
    @ApiModelProperty(value = "订单对应商品list", position = 26)
    @JSONField(ordinal = 26)
    private List<OrderShopVO> orderShopVOList;

    /*订单对应提货信息表：order_pick*/
    @ApiModelProperty(value = "订单对应提货信息表list", position = 27)
    @JSONField(ordinal = 27)
    private List<OrderPickVO> orderPickVOList;


    /*订单对应报关文件：order_customs_file*/
    @ApiModelProperty(value = "订单对应报关文件list", position = 28)
    @JSONField(ordinal = 28)
    private List<OrderCustomsFileVO> orderCustomsFileVOList;

    /*订单对应清关文件：order_clearance_file*/
    @ApiModelProperty(value = "订单对应清关文件list", position = 29)
    @JSONField(ordinal = 29)
    private List<OrderClearanceFileVO> orderClearanceFileVOList;


}
