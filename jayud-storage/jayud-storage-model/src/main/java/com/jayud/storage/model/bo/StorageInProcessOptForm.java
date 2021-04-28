package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 入库节点操作流程
 */
@Data
@Slf4j
public class StorageInProcessOptForm {

    @NotNull(message = "主订单id不能为空")
    @ApiModelProperty(value = "主订单id", required = true)
    private Long mainOrderId;

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

    @ApiModelProperty(value = "操作情况 submit 提交  end 完结")
    private String cmd;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    //入仓信息

    @ApiModelProperty(value = "订单详情主键id")
    private Long id;

    @ApiModelProperty(value = "仓库id")
    private String warehouseId;

    @ApiModelProperty(value = "操作id")
    private List<Long> operationId;

    @ApiModelProperty(value = "卡板类型id（可多选，以逗号隔开）")
    private List<Long> cardtypeId;

    @ApiModelProperty(value = "YES(1为是，2为否)")
    private Integer yes;

    @ApiModelProperty(value = "NO(1为是，2为否)")
    private Integer no;

    @ApiModelProperty(value = "已影相(1为是，2为否)")
    private Integer isGone;

    @ApiModelProperty(value = "(不可叠放)指示(1为是，2为否)")
    private Integer isInstructions;

    @ApiModelProperty(value = "数量1")
    private Integer num1;

    @ApiModelProperty(value = "上门收件(1为是，2为否)")
    private Integer isDoorCollection;

    @ApiModelProperty(value = "客户自送(1为是，2为否)")
    private Integer isSelfDelivery;

    @ApiModelProperty(value = "已贴Gold Labels(1为是，2为否)")
    private Integer isGoldLabels;

    @ApiModelProperty(value = "包装不当(1为是，2为否)")
    private Integer isImproperPacking;

    @ApiModelProperty(value = "数量2")
    private Integer num2;

    @ApiModelProperty(value = "Tom Open 已开口(1为是，2为否)")
    private Integer isTomOpen;

    @ApiModelProperty(value = "Tom Open 已开口的数量")
    private Integer tomOpenNumber;

    @ApiModelProperty(value = "Re Taped 重贴胶纸(1为是，2为否)")
    private Integer isReTaped;

    @ApiModelProperty(value = "Crushed Collapsed 已压破/摺曲(1为是，2为否)")
    private Integer isCrushedCollapsed;

    @ApiModelProperty(value = "Crushed Collapsed 已压破/摺曲的数量")
    private Integer crushedCollapsedNumber;

    @ApiModelProperty(value = "Re Taped 重贴胶纸的数量")
    private Integer reTapedNumber;

    @ApiModelProperty(value = "Water Greased 有水渍/油渍(1为是，2为否)")
    private Integer isWaterGreased;

    @ApiModelProperty(value = "Water Greased 有水渍/油渍的数量")
    private Integer waterGreasedNumber;

    @ApiModelProperty(value = "Punctured/Holes 外箱破损/有洞(1为是，2为否)")
    private Integer isPuncturedHoles;

    @ApiModelProperty(value = "Punctured/Holes 外箱破损/有洞的数量")
    private Integer puncturedHolesNumber;

    @ApiModelProperty(value = "Damaged Ctn.No.PCS(1为是，2为否)")
    private Integer isDamagedCtn;

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

    @ApiModelProperty(value = "商品入库")
    private List<WarehouseGoodsForm> warehouseGoodsForms;


    public void setStatus(String status) {
        this.status = status;
        this.statusName = OrderStatusEnum.getDesc(status);
    }

    public void checkProcessOpt(OrderStatusEnum statusEnum) {
        boolean pass = true;
        switch (statusEnum) {
            case CCI_1: //入库接单
            case CCI_2: //确认入仓
            case CCI_3: //仓储入库
                pass = checkOptInfo();
                break;
        }
        if (!pass) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
    }



    public boolean checkOptInfo() {
        if (StringUtils.isEmpty(this.operatorUser)) {
            log.warn("操作人必填");
            return false;
        }
        if (StringUtils.isEmpty(this.operatorTime)) {
            log.warn("操作时间必填");
            return false;
        }
        return true;
    }
}
