package com.jayud.storage.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 入仓数据返回
 */
@Data
@Slf4j
public class StorageInProcessOptFormVO {

    @NotNull(message = "主订单id不能为空")
    @ApiModelProperty(value = "主订单id", required = true)
    private String mainOrderId;

    @ApiModelProperty(value = "主订单号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "入库订单号", required = true)
    private String orderNo;

    @NotNull(message = "入库订单id不能为空")
    @ApiModelProperty(value = "入库订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "业务类型(0:空运),(1,纯报关),(2,中港运输) 前端不用管")
    private Integer businessType;

    @ApiModelProperty(value = "状态码,前台不用传")
    private String status;

    @ApiModelProperty(value = "状态名称,前台不用传")
    private String statusName;

    @ApiModelProperty(value = "附件,前台不用传")
    private String statusPic;

    @ApiModelProperty(value = "附件名称,前台不用传")
    private String statusPicName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "入仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "确认入仓是否提交（0未提交，1已提交）")
    private Integer isSubmit;


    //入仓信息
    @ApiModelProperty(value = "订单详情主键id")
    private Long id;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库电话")
    private String warehousePhone;

    @ApiModelProperty(value = "仓库地址")
    private String warehouseAddress;

    @ApiModelProperty(value = "操作id")
    private List<Long> operationId;

    @ApiModelProperty(value = "卡板类型id（可多选，以逗号隔开）")
    private List<Long> cardTypeId;

    @ApiModelProperty(value = "YES(1为是，2为否)")
    private Boolean yes;

    @ApiModelProperty(value = "NO(1为是，2为否)")
    private Boolean no;

    @ApiModelProperty(value = "已影相(1为是，2为否)")
    private Boolean isGone;

    @ApiModelProperty(value = "(不可叠放)指示(1为是，2为否)")
    private Boolean isInstructions;

    @ApiModelProperty(value = "数量1")
    private Integer num1;

    @ApiModelProperty(value = "上门收件(1为是，2为否)")
    private Boolean isDoorCollection;

    @ApiModelProperty(value = "客户自送(1为是，2为否)")
    private Boolean isSelfDelivery;

    @ApiModelProperty(value = "已贴Gold Labels(1为是，2为否)")
    private Boolean isGoldLabels;

    @ApiModelProperty(value = "包装不当(1为是，2为否)")
    private Boolean isImproperPacking;

    @ApiModelProperty(value = "数量2")
    private Integer num2;

    @ApiModelProperty(value = "Tom Open 已开口(1为是，2为否)")
    private Boolean isTomOpen;

    @ApiModelProperty(value = "Tom Open 已开口的数量")
    private Integer tomOpenNumber;

    @ApiModelProperty(value = "Re Taped 重贴胶纸(1为是，2为否)")
    private Boolean isReTaped;

    @ApiModelProperty(value = "Crushed Collapsed 已压破/摺曲(1为是，2为否)")
    private Boolean isCrushedCollapsed;

    @ApiModelProperty(value = "Crushed Collapsed 已压破/摺曲的数量")
    private Integer crushedCollapsedNumber;

    @ApiModelProperty(value = "Re Taped 重贴胶纸的数量")
    private Integer reTapedNumber;

    @ApiModelProperty(value = "Water Greased 有水渍/油渍(1为是，2为否)")
    private Boolean isWaterGreased;

    @ApiModelProperty(value = "Water Greased 有水渍/油渍的数量")
    private Integer waterGreasedNumber;

    @ApiModelProperty(value = "Punctured/Holes 外箱破损/有洞(1为是，2为否)")
    private Boolean isPuncturedHoles;

    @ApiModelProperty(value = "Punctured/Holes 外箱破损/有洞的数量")
    private Integer puncturedHolesNumber;

    @ApiModelProperty(value = "Damaged Ctn.No.PCS(1为是，2为否)")
    private Boolean isDamagedCtn;

    @ApiModelProperty(value = "Damaged Ctn.No.PCS的数量")
    private Integer damagedCtnNumber;

    @ApiModelProperty(value = "备注")
    private String beizhu;

    @ApiModelProperty(value = "Remarks")
    @TableField("Remarks")
    private String remarks;

    @ApiModelProperty(value = "Marks")
    @TableField("Marks")
    private String marks;

    @ApiModelProperty(value = "单证")
    private String documents;

    @ApiModelProperty(value = "仓管")
    private String warehouseManagement;

    @ApiModelProperty(value = "司机")
    private String driver;

    @ApiModelProperty(value = "商品入库信息")
    private List<WarehouseGoodsVO> warehouseGoodsForms;

    @ApiModelProperty(value = "商品实际入仓信息")
    private List<InGoodsOperationRecordVO> inGoodsOperationRecords;

    public void setStatus(String status) {
        this.status = status;
        this.statusName = OrderStatusEnum.getDesc(status);
    }

    /**
     * @param mainOrderObjs 远程客户对象集合
     */
    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                this.customerCode = json.getStr("customerCode");
                this.mainOrderId = json.getStr("id");
                break;
            }
        }

    }

}
