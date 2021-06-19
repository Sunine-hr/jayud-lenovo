package com.jayud.oms.model.vo.template.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.vo.WarehouseGoodsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 创建订单，快进快出表单
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
@Data
public class StorageFastTemplate extends BaseOrderTemplate {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态()")
    private String status;

    @ApiModelProperty(value = "订单状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态名")
    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位code")
    @NotNull(message = "结算单位不为空")
    private String unitCode;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    @NotNull(message = "操作主体不为空")
    private Long legalEntityId;

    @ApiModelProperty(value = "操作部门id")
    @NotNull(message = "操作部门不为空")
    private Long departmentId;

    @ApiModelProperty(value = "操作部门")
    private String departmentName;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "入库车牌号")
    private String inPlateNumber;

    @ApiModelProperty(value = "入仓号")
    @NotNull(message = "入仓号不为空")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "预计到达时间")
    private String estimatedArrivalTime;

    @ApiModelProperty(value = "预计出库时间")
    private String expectedDeliveryTime;

    @ApiModelProperty(value = "出库车牌号")
    private String outPlateNumber;

    @ApiModelProperty(value = "出仓号")
    @NotNull(message = "出仓号不为空")
    private String outWarehouseNumber;

    @ApiModelProperty(value = "是否入库（1为入库，0为不入库）")
    private Integer isWarehouse;

    @ApiModelProperty(value = "快进快出商品对象集合")
    private List<WarehouseGoodsVO> fastGoodsFormList;

    @ApiModelProperty(value = "入库商品对象集合")
    private List<WarehouseGoodsVO> inGoodsFormList;

    @ApiModelProperty(value = "出库商品对象集合")
    private List<WarehouseGoodsVO> outGoodsFormList;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> allPics;

    @ApiModelProperty(value = "费用状态", required = true)
    private String costDesc;

    @ApiModelProperty(value = "费用状态")
    private Boolean cost;

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName=unitCodeName;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

}
